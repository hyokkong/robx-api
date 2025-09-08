package com.rbox.common.qr;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.client.j2se.MatrixToSvgWriter;

/**
 * Simple QR code generation service using ZXing.
 */
@Service
public class QrCodeService {
    public byte[] generate(String text, int width, String fmt) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, width);
            if ("svg".equalsIgnoreCase(fmt)) {
                String svg = MatrixToSvgWriter.toSvgString(matrix);
                return svg.getBytes(StandardCharsets.UTF_8);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("QR generation failed", e);
        }
    }
}
