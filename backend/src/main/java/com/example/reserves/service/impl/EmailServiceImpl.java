package com.example.reserves.service.impl;

import com.example.reserves.model.Appointment;
import com.example.reserves.service.EmailService;
import com.example.reserves.utils.EmailTemplates;
import com.example.reserves.utils.IcsGenerator;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final Resend resend;
    private final EmailTemplates emailTemplates;
    private final IcsGenerator icsGenerator;

    @Value("${resend.from}")
    private String from;

    public EmailServiceImpl(@Value("${resend.api-key}") String apiKey,
                            EmailTemplates emailTemplates,
                            IcsGenerator icsGenerator) {
        this.resend = new Resend(apiKey);
        this.emailTemplates = emailTemplates;
        this.icsGenerator = icsGenerator;
    }

    @Override
    public void sendConfirmation(Appointment appointment) {
        try {
            System.out.println("📧 Intentando enviar email a: " + appointment.getClientEmail());
            System.out.println("📧 API Key: " + from);

            String icsContent = icsGenerator.generate(appointment);
            String icsBase64 = Base64.getEncoder()
                    .encodeToString(icsContent.getBytes());

            Attachment icsAttachment = Attachment.builder()
                    .fileName("cita.ics")
                    .content(icsBase64)
                    .build();

            CreateEmailOptions request = CreateEmailOptions.builder()
                    .from(from)
                    .to(appointment.getClientEmail())
                    .subject("✂️ Confirmación de cita — " +
                            appointment.getLocation().getLocationName())
                    .html(emailTemplates.confirmationHtml(appointment))
                    .attachments(List.of(icsAttachment))
                    .build();

            var response = resend.emails().send(request);
            System.out.println("✅ Email enviado, ID: " + response.getId());

        } catch (ResendException e) {
            System.err.println("❌ Error enviando email: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendReminder(Appointment appointment) {
        try {
            CreateEmailOptions request = CreateEmailOptions.builder()
                    .from(from)
                    .to(appointment.getClientEmail())
                    .subject("⏰ Recordatorio — Cita mañana en " +
                            appointment.getLocation().getLocationName())
                    .html(emailTemplates.reminderHtml(appointment))
                    .build();

            resend.emails().send(request);

        } catch (ResendException e) {
            System.err.println("❌ Error enviando recordatorio: " + e.getMessage());
        }
    }
}