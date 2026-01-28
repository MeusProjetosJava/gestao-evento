package vitor.gestaoevento.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.exception.*;
import vitor.gestaoevento.infra.qrcode.QrCodeService;
import vitor.gestaoevento.integration.sms.SmsService;
import vitor.gestaoevento.model.Event;
import vitor.gestaoevento.model.Registration;
import vitor.gestaoevento.model.PaymentStatus;
import vitor.gestaoevento.model.User;
import vitor.gestaoevento.repository.EventRepository;
import vitor.gestaoevento.repository.RegistrationRepository;
import vitor.gestaoevento.repository.UserRepository;
import vitor.gestaoevento.security.AuthenticatedUserService;

@Slf4j
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final SmsService smsService;
    private final QrCodeService qrCodeService;
    private final AuthenticatedUserService authenticatedUserService;


    public RegistrationService(UserRepository userRepository,
                               EventRepository eventRepository,
                               RegistrationRepository registrationRepository,
                               SmsService smsService, QrCodeService qrCodeService,
                               AuthenticatedUserService authenticatedUserService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.smsService = smsService;
        this.qrCodeService = qrCodeService;
        this.authenticatedUserService = authenticatedUserService;
    }

    public Registration registerParticipation(Long eventId) {

        User authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        if (!event.isActive()) {
            throw new InactiveEventException("O evento não está ativo");
        }

        boolean participationAlreadyExists =
                registrationRepository.existsByUserAndEvent(authenticatedUser, event);

        if (participationAlreadyExists) {
            throw new UserAlreadyEnrolledException("Usuário já está inscrito neste evento");
        }

        Registration registration = new Registration(authenticatedUser, event);

        return registrationRepository.save(registration);
    }

    @Transactional
    public Registration confirmPayment(Long participationId) {

        Registration registration = registrationRepository.findById(participationId)
                .orElseThrow(() -> new RegistrationNotFoundException("Participação não encontrada"));

        registration.confirmPayment();

        Registration registrationSaved = registrationRepository.save(registration);

        smsService.sendPaymentConfirmation(registrationSaved);


        return registrationSaved;
    }

    public void checkIn(String qrCode) {
        Long participationId = extractParticipationId(qrCode);

        Registration registration = registrationRepository.findById(participationId).orElseThrow(()
        -> new RegistrationNotFoundException("Participação não encontrada"));

        registration.performCheckIn();

        registrationRepository.save(registration);


    }

    private Long extractParticipationId(String qrCode) {

        if (qrCode == null || qrCode.isBlank()) {
            throw new InvalidQrCodeException("QR Code inválido");
        }


        if (!qrCode.startsWith("participacao:")) {
            throw new InvalidQrCodeException("QR Code com formato inválido");
        }

        String[] parts = qrCode.split(":");

        if (parts.length != 2) {
            throw new InvalidQrCodeException("QR Code malformado");
        }

        try {
            return Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            throw new InvalidQrCodeException("ID da participação inválido");
        }
    }



    public void validateForQrGeneration(Registration registration) {
        if (registration.getPaymentStatus() != PaymentStatus.PAID) {
            throw new InvalidQrCodeException("Qr code só pode ser gerado com o pagamento pago");
        }

        if (!registration.getEvent().isActive()){
            throw new InactiveEventException("Evento não está ativo");
        }

    }

    public byte[] generateParticipationQrCode(Long participationId) {
        Registration registration = registrationRepository.findById(participationId)
                .orElseThrow(() -> new RegistrationNotFoundException("Participação não encontrada"));

        User authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        if (!registration.getUser().getId().equals(authenticatedUser.getId())){
            throw new SecurityException("Você não tem permissão para gerar este QR Code");
        }

        validateForQrGeneration(registration);

        String conteudoQr = "participacao:" + registration.getId();

        return qrCodeService.generateQrCodePng(conteudoQr);
    }

}
