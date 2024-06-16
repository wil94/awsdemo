package com.example.ai.awsdemo.controller;


import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.visible.PDVisibleSigProperties;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.visible.PDVisibleSignDesigner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import java.security.KeyStore;
import java.security.PrivateKey;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.security.cert.X509Certificate;
import java.util.Date;

@RestController
@RequestMapping("/one-drive-files")


public class OneDriveController {

    @GetMapping
    public String dummyGreeting() {
        return "service ai is up!";
    }

    @PostMapping("sign")
    public String signDoc() {
        try {
            Security.addProvider(new BouncyCastleProvider());

            File file = new File("D:/tmp1/GIT.pdf");
            PDDocument doc = PDDocument.load(file);

            // Load the keystore
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(new FileInputStream("D:/tmp1/softoken.p12"), "Sabrina1".toCharArray());


            String alias = keystore.aliases().nextElement();

            // Obtener el certificado
            X509Certificate cert = (X509Certificate) keystore.getCertificate(alias);

            // Fechas de validez del certificado
            Date notBefore = cert.getNotBefore();
            Date notAfter = cert.getNotAfter();
            System.out.println("El certificado es válido desde: " + notBefore);
            System.out.println("El certificado es válido hasta: " + notAfter);

            // Verificar si el certificado está vigente
            cert.checkValidity();
            System.out.println("El certificado está actualmente vigente");

            // Create a new signature
            PDSignature signature = new PDSignature();
            signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
            signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
            signature.setName("Example User");
            signature.setLocation("Example Location");
            signature.setReason("Example Reason");
            signature.setSignDate(Calendar.getInstance());

            // Sign the PDF
            File signedPdf = new File("D:/tmp1/signed.pdf");
            SignatureOptions options = new SignatureOptions();
            options.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2);
            //doc.addSignature(signature, new PDFBoxSignatureCreator(keystore, "tu_contraseña".toCharArray()), options);

            //options.setVisualSignature(createVisibleSignatureTemplate(doc, 0, signature));
            options.setPage(0); // Sign first page

            //doc.addSignature(signature, new SigningSupport(keystore), options);


            doc.save(signedPdf);
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR " + e.getMessage());
        }
        return "OK";
    }

    private static PDVisibleSigProperties createVisibleSignatureTemplate(PDDocument doc, int pageNumber, PDSignature signature) throws IOException, FileNotFoundException {
        PDVisibleSignDesigner visibleSignDesigner = new PDVisibleSignDesigner(doc, new FileInputStream("D:/tm/signature.jpg"), pageNumber);
        visibleSignDesigner.xAxis(100).yAxis(100).zoom(-50).signatureFieldName("signature");

        PDVisibleSigProperties signatureProperties = new PDVisibleSigProperties();
        signatureProperties.signerName(signature.getName()).signerLocation(signature.getLocation()).signatureReason(signature.getReason())
                .preferredSize(0).page(pageNumber).visualSignEnabled(true).setPdVisibleSignature(visibleSignDesigner);

        return signatureProperties;
    }

}
