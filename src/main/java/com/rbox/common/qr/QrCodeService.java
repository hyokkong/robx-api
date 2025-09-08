package com.rbox.common.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class QrCodeService {

    /**
     * @param text  QR에 들어갈 텍스트
     * @param width 원하는 출력 픽셀 가로/세로 크기 (PNG의 경우 최대 크기)
     * @param fmt   "svg" 또는 그 외("png" 등)
     */
    public byte[] generate(String text, int width, String fmt) {
        try {
            // 비트매트릭스 생성 (모듈 크기는 ZXing이 계산)
            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, width);

            if ("svg".equalsIgnoreCase(fmt)) {
                // SVG는 JDK 문자열로 직접 생성
                String svg = bitMatrixToSvg(matrix, 4, 4); // pixelSize, margin (원하면 조정)
                return svg.getBytes(StandardCharsets.UTF_8);
            } else {
                // PNG: 요청된 width를 기준으로 스케일링해서 BufferedImage로 변환
                return bitMatrixToPng(matrix, width);
            }
        } catch (Exception e) {
            throw new RuntimeException("QR generation failed", e);
        }
    }

    // ------------------------
    // BitMatrix -> SVG 변환 (MatrixToSvgWriter 대체)
    // pixelSize : 한 모듈(셀)의 픽셀 크기
    // margin : 모듈 외부 여백(모듈 단위)
    private String bitMatrixToSvg(BitMatrix matrix, int pixelSize, int margin) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int svgWidth = (w + margin * 2) * pixelSize;
        int svgHeight = (h + margin * 2) * pixelSize;

        StringBuilder sb = new StringBuilder(1024);
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append(String.format("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 %d %d\" width=\"%d\" height=\"%d\">\n",
                svgWidth, svgHeight, svgWidth, svgHeight));
        sb.append("<rect width=\"100%\" height=\"100%\" fill=\"#ffffff\"/>\n");
        sb.append("<g fill=\"#000000\">\n");

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (matrix.get(x, y)) {
                    int rx = (x + margin) * pixelSize;
                    int ry = (y + margin) * pixelSize;
                    sb.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" />\n",
                            rx, ry, pixelSize, pixelSize));
                }
            }
        }

        sb.append("</g>\n</svg>");
        return sb.toString();
    }

    // ------------------------
    // BitMatrix -> PNG 바이트 (MatrixToImageWriter 대체)
    private byte[] bitMatrixToPng(BitMatrix matrix, int targetSizePx) throws Exception {
        int matrixW = matrix.getWidth();
        int matrixH = matrix.getHeight();

        // 스케일: 요청된 픽셀 크기에 맞게 각 모듈을 늘림 (최소 1)
        int scale = Math.max(1, targetSizePx / Math.max(matrixW, matrixH));
        int imgW = matrixW * scale;
        int imgH = matrixH * scale;

        BufferedImage image = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);

        // 배경 흰색으로 채우기
        int white = 0xFFFFFF;
        int black = 0x000000;
        for (int y = 0; y < imgH; y++) {
            for (int x = 0; x < imgW; x++) {
                image.setRGB(x, y, white);
            }
        }

        // 각 모듈을 스케일만큼 채우기
        for (int my = 0; my < matrixH; my++) {
            for (int mx = 0; mx < matrixW; mx++) {
                if (matrix.get(mx, my)) {
                    int startX = mx * scale;
                    int startY = my * scale;
                    for (int yy = 0; yy < scale; yy++) {
                        for (int xx = 0; xx < scale; xx++) {
                            image.setRGB(startX + xx, startY + yy, black);
                        }
                    }
                }
            }
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
        }
    }
}