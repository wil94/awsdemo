package com.example.ai.awsdemo.controller;

import com.example.ai.awsdemo.model.dto.QueryResponseDto;
import com.example.ai.awsdemo.service.AnalyzeDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    @Autowired
    AnalyzeDocument analyzeDocument;

    @GetMapping
    public String dummyGreeting() {
        return "service ai is up!";
    }

    @PostMapping("/queries")
    public @ResponseBody
    ResponseEntity<List<QueryResponseDto>> getTextUsingQueries() {
        //analyzeDocument.analyzeDoc(null, "D:/tmp/ruat.png");
        List<QueryResponseDto> resp = analyzeDocument.runTextractExample();
        return new ResponseEntity<List<QueryResponseDto>>(resp, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadBase64File() {


        String filePath = "D:/Development/Java/awsdemo/src/main/java/com/example/ai/awsdemo/controller/b64.txt";  // Ruta al archivo con la caden base64

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder base64Content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                base64Content.append(line);
            }

            String base64EncodedString = base64Content.toString(); // o directamente el b64

            //byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
            //String decodedText = new String(decodedBytes);

            //System.out.println("Base64 Decoded Text:\n" + decodedText);



            String base64String = ""; // Coloca aquí tu cadena base64

            byte[] fileBytes = java.util.Base64.getDecoder().decode(base64EncodedString);
            ByteArrayResource resource = new ByteArrayResource(fileBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=downloaded-file.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileBytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

}
