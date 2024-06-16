package com.example.ai.awsdemo.service;

// snippet-start:[textract.java2._analyze_doc.import]
import com.example.ai.awsdemo.model.dto.AnswerResponseDto;
import com.example.ai.awsdemo.model.dto.QueryResponseDto;
import com.example.ai.awsdemo.model.entity.QueryEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyzeDocument {

    public List<QueryResponseDto> runTextractExample() {
        String sourceDoc = "D:\\tmp\\inv3.png";
        Region region = Region.US_EAST_1;

        TextractClient textractClient = TextractClient.builder()
                .endpointOverride(URI.create("https://textract.us-east-1.amazonaws.com"))
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create()) // here we can add "profilename" into create
                .build();

        System.out.println("dev 1 " + textractClient.serviceName());

        //List<QueryResponseDto> resp = analyzeDoc(textractClient, sourceDoc);

        textractClient.close();
        return null;
    }

    // snippet-start:[textract.java2._analyze_doc.main]
    public List<QueryResponseDto> analyzeDoc(TextractClient textractClient, String sourceDoc) {

        try {
            // Read a file using its path directory
            InputStream sourceStream = new FileInputStream(new File(sourceDoc));
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

            // Get the input Document object as bytes
            // TODO convert b64 to bytes and send directly here, if it is required
            Document myDoc = Document.builder()
                    .bytes(sourceBytes)
                    .build();

            List<FeatureType> featureTypes = new ArrayList<FeatureType>();
            featureTypes.add(FeatureType.QUERIES);
            /**
             * to get blocks with analysis of tables and forms
             * featureTypes.add(FeatureType.FORMS);
             * featureTypes.add(FeatureType.TABLES);
             */

            List<Query> queries = getQueriesForAWSTextractSerice();
            if (queries.size() == 0) return null;

            //queries.clear();
            //queries.add(Query.builder().text("Cual es el modelo?").alias("RUAT_MODELO").build());

            QueriesConfig queriesConfig = QueriesConfig.builder()
                    .queries(queries)
                    .build();

            AnalyzeDocumentRequest analyzeDocumentRequest = AnalyzeDocumentRequest.builder()
                    .featureTypes(featureTypes)
                    .queriesConfig(queriesConfig)
                    .document(myDoc)
                    .build();


            AnalyzeDocumentResponse analyzeDocument = textractClient.analyzeDocument(analyzeDocumentRequest);
            List<Block> docInfo = analyzeDocument.blocks();
            List<QueryResponseDto> response = getResponse(docInfo);

            Iterator<Block> blockIterator = docInfo.iterator();

            while(blockIterator.hasNext()) {
                Block block = blockIterator.next();
                System.out.println("The block type is " +block.blockType().toString());
            }

            return response;


            //return  null;

        } catch (TextractException | FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    public List<QueryEntity> getQueriesByService(String service) {
        // TODO get this from DB
        //List<QueryEntity> result = new ArrayList<>();
        /*
        QueryEntity q1 = new QueryEntity(1, "placa", "RUAT_PLACA", "Cual es la placa?", "AWSTEXTRACT");
        QueryEntity q2 = new QueryEntity(2, "clase de vehículo", "RUAT_CLASE", "Cual es la clase?", "AWSTEXTRACT");
        QueryEntity q3 = new QueryEntity(3, "marca", "RUAT_MARCA", "que Marca de vehículo es?", "AWSTEXTRACT");
        QueryEntity q4 = new QueryEntity(4, "modelo", "RUAT_MODELO", "Cual es el modelo?", "AWSTEXTRACT");
        QueryEntity q5 = new QueryEntity(5, "Número de motor", "RUAT_MOTOR", "Indique el número de motor", "AWSTEXTRACT");
        QueryEntity q6 = new QueryEntity(6, "Número de chasis", "RUAT_CHASIS", "Numero Chasis", "AWSTEXTRACT");
        QueryEntity q7 = new QueryEntity(7, "Servicio que presta el vehículo", "RUAT_SERVICIO", "Que tipo de servicio ofrece?", "AWSTEXTRACT");
        result.add(q1);
        result.add(q2);
        //result.add(q3);
        result.add(q4);
        //result.add(q5);
        //result.add(q6);
        //result.add(q7);
        //return result;


         */


        // TODO get this from DB
        // Query value do not must be greater than 200 chars
        // Query value do not must have special chars as '´´...
        List<QueryEntity> result = new ArrayList<>();
        QueryEntity q1 = new QueryEntity(1, "nit", "NIT", "NIT cabecera", "AWS_TEXTRACT_RUAT");
        QueryEntity q2 = new QueryEntity(2, "factura", "FACTURA", "numero Factura", "AWS_TEXTRACT_RUAT");
        QueryEntity q3 = new QueryEntity(3, "factura1", "FACTURA", "Factura no", "AWS_TEXTRACT_RUAT");
        QueryEntity q4 = new QueryEntity(4, "autorizacion", "AUTORIZACION", "Numero autorizacion", "AWS_TEXTRACT_RUAT");
        QueryEntity q5 = new QueryEntity(5, "autorizacion1", "AUTORIZACION", "Cod Autorizacion", "AWS_TEXTRACT_RUAT");
        QueryEntity q6 = new QueryEntity(6, "fecha", "FECHA", "lugar y Fecha emision", "AWS_TEXTRACT_RUAT");
        QueryEntity q7 = new QueryEntity(7, "fecha1", "FECHA", "Fecha?", "AWS_TEXTRACT_RUAT");
        QueryEntity q8 = new QueryEntity(8, "montobs", "MONTO", "monto BS", "AWS_TEXTRACT_RUAT");
        QueryEntity q9 = new QueryEntity(9, "montosus", "MONTO", "monto Sus o USD", "AWS_TEXTRACT_RUAT");
        QueryEntity q10 = new QueryEntity(9, "total", "MONTO", "total", "AWS_TEXTRACT_RUAT");
        result.add(q1);
        result.add(q2);
        result.add(q3);
        result.add(q4);
        result.add(q5);
        result.add(q6);
        result.add(q7);
        result.add(q8);
        result.add(q9);
        result.add(q10);
        return result;

    }

    public List<Query> getQueriesForAWSTextractSerice() {
        List<Query> queries = new ArrayList<>();
        List<QueryEntity> queriesFromDB = getQueriesByService("AWSTEXTRACT");
        if (queriesFromDB.size() == 0) return queries;

        queriesFromDB.forEach((queryFromDB) -> {
            queries.add(Query.builder().text(queryFromDB.getValue()).alias(queryFromDB.getAlias()).build());
        });
        return queries;
    }

    public List<Block> getBlocksInfoByType(List<Block> blocks, BlockType type) {
        return blocks.stream()
                .filter(block -> block.blockType().equals(type))
                .collect(Collectors.toList());
    }

    public List<QueryResponseDto> getResponse(List<Block> blocks) {
        List<QueryResponseDto> result = new ArrayList<>();
        List<Block> queries = getBlocksInfoByType(blocks, BlockType.QUERY);
        List<Block> answers = getBlocksInfoByType(blocks, BlockType.QUERY_RESULT);

        queries.forEach((query) -> {
            if (query.query() != null) {
                for (Relationship relationship: query.relationships()) {
                    if (relationship != null && relationship.type().equals(RelationshipType.ANSWER)) {
                        List<String> idsOfAnswersForThisQuery = relationship.ids();
                        for (String idAnswer : idsOfAnswersForThisQuery) {
                            // get answer for this query by relation id
                            List<Block> answersForQuery = answers.stream()
                                    .filter(answer -> answer.id().equals(idAnswer))
                                    .collect(Collectors.toList());

                            // if there are answers for this query
                            if (answersForQuery != null && answersForQuery.size() > 0) {
                                Query madeQuery = query.query();
                                QueryResponseDto queryResponseDto = new QueryResponseDto();
                                // prepare query attributes
                                queryResponseDto.setQueryId(query.id());
                                queryResponseDto.setQuery(madeQuery.text());
                                queryResponseDto.setQueryAlias(madeQuery.alias());
                                // prepare answer attributes
                                List<AnswerResponseDto> answersForMadeQuery = new ArrayList<>();
                                answersForQuery.forEach((answer) -> {
                                    AnswerResponseDto answerResponseDto = new AnswerResponseDto();
                                    answerResponseDto.setAnswer(answer.text());
                                    answerResponseDto.setAnswerId(answer.id());
                                    answerResponseDto.setConfidence(Double.valueOf(answer.confidence()));
                                    answersForMadeQuery.add(answerResponseDto);
                                });
                                // add to result
                                queryResponseDto.setAnswers(answersForMadeQuery);
                                result.add(queryResponseDto);
                            } else return; // if answer not found for query go to next
                        }
                    }
                }
            }
        });
        return result;
    }

}
