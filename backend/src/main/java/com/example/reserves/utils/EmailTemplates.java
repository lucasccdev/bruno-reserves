package com.example.reserves.utils;

import com.example.reserves.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class EmailTemplates {

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");

    public String confirmationHtml(Appointment a) {
        String fecha = a.getStartTime().format(DISPLAY_FORMAT);
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <style>
                    body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 40px auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .header { background: #1a1a1a; color: white; padding: 32px; text-align: center; }
                    .header h1 { margin: 0; font-size: 24px; letter-spacing: 2px; }
                    .header p { margin: 8px 0 0; color: #aaa; font-size: 14px; }
                    .body { padding: 32px; }
                    .body h2 { color: #1a1a1a; margin-top: 0; }
                    .detail { background: #f9f9f9; border-radius: 8px; padding: 20px; margin: 20px 0; }
                    .detail-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #eee; }
                    .detail-row:last-child { border-bottom: none; }
                    .label { color: #666; font-size: 14px; }
                    .value { color: #1a1a1a; font-weight: bold; font-size: 14px; }
                    .ics-note { background: #e8f4e8; border-radius: 8px; padding: 16px; margin: 20px 0; font-size: 14px; color: #2d6a2d; }
                    .footer { background: #f9f9f9; padding: 20px 32px; text-align: center; font-size: 12px; color: #999; }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <h1>✂️ BARBERÍA</h1>
                      <p>Confirmación de cita</p>
                    </div>
                    <div class="body">
                      <h2>¡Hola, %s!</h2>
                      <p>Tu cita ha sido confirmada. Aquí tienes los detalles:</p>
                      <div class="detail">
                        <div class="detail-row">
                          <span class="label">📅 Fecha y hora</span>
                          <span class="value">%s</span>
                        </div>
                        <div class="detail-row">
                          <span class="label">✂️ Servicio</span>
                          <span class="value">%s</span>
                        </div>
                        <div class="detail-row">
                          <span class="label">💈 Barbero</span>
                          <span class="value">%s</span>
                        </div>
                        <div class="detail-row">
                          <span class="label">📍 Local</span>
                          <span class="value">%s</span>
                        </div>
                        <div class="detail-row">
                          <span class="label">⏱️ Duración</span>
                          <span class="value">%d minutos</span>
                        </div>
                      </div>
                      <div class="ics-note">
                        📆 Adjuntamos un archivo <strong>.ics</strong> para que puedas añadir la cita directamente a tu calendario (iOS, Android, Google Calendar...).
                      </div>
                    </div>
                    <div class="footer">
                      Si necesitas cancelar o modificar tu cita, contacta con nosotros.<br>
                      %s · %s
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(
                a.getClientName(),
                fecha,
                a.getService().getServiceName(),
                a.getBarber().getBarberName(),
                a.getLocation().getLocationName(),
                a.getService().getDuration(),
                a.getLocation().getLocationName(),
                a.getLocation().getPhone()
        );
    }

    public String reminderHtml(Appointment a) {
        String fecha = a.getStartTime().format(DISPLAY_FORMAT);
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <style>
                    body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 40px auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .header { background: #b8860b; color: white; padding: 32px; text-align: center; }
                    .header h1 { margin: 0; font-size: 24px; letter-spacing: 2px; }
                    .body { padding: 32px; }
                    .detail { background: #f9f9f9; border-radius: 8px; padding: 20px; margin: 20px 0; }
                    .detail-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #eee; }
                    .detail-row:last-child { border-bottom: none; }
                    .label { color: #666; font-size: 14px; }
                    .value { color: #1a1a1a; font-weight: bold; font-size: 14px; }
                    .footer { background: #f9f9f9; padding: 20px 32px; text-align: center; font-size: 12px; color: #999; }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <h1>⏰ RECORDATORIO DE CITA</h1>
                    </div>
                    <div class="body">
                      <h2>¡Hola, %s!</h2>
                      <p>Te recordamos que mañana tienes una cita en la barbería:</p>
                      <div class="detail">
                        <div class="detail-row">
                          <span class="label">📅 Fecha y hora</span>
                          <span class="value">%s</span>
                        </div>
                        <div class="detail-row">
                          <span class="label">✂️ Servicio</span>
                          <span class="value">%s</span>
                        </div>
                        <div class="detail-row">
                          <span class="label">💈 Barbero</span>
                          <span class="value">%s</span>
                        </div>
                        <div class="detail-row">
                          <span class="label">📍 Local</span>
                          <span class="value">%s</span>
                        </div>
                      </div>
                    </div>
                    <div class="footer">
                      %s · %s
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(
                a.getClientName(),
                fecha,
                a.getService().getServiceName(),
                a.getBarber().getBarberName(),
                a.getLocation().getLocationName(),
                a.getLocation().getLocationName(),
                a.getLocation().getPhone()
        );
    }
}