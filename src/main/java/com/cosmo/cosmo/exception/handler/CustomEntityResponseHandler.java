package com.cosmo.cosmo.exception.handler;

import com.cosmo.cosmo.exception.ErrorResponse;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.exception.ValidationException;
import com.cosmo.cosmo.exception.DuplicateResourceException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
public class CustomEntityResponseHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Dados inválidos fornecidos",
                request.getRequestURI()
        );

        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ErrorResponse.FieldError(
                    error.getField(),
                    error.getRejectedValue(),
                    error.getDefaultMessage()
            ));
        }
        errorResponse.setFieldErrors(fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de binding",
                "Erro ao processar os dados da requisição",
                request.getRequestURI()
        );

        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ErrorResponse.FieldError(
                    error.getField(),
                    error.getRejectedValue(),
                    error.getDefaultMessage()
            ));
        }
        errorResponse.setFieldErrors(fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        String message = "Violação de integridade dos dados";
        String error = "Erro de integridade";
        Map<String, Object> details = new HashMap<>();

        // Tenta extrair informação mais específica
        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            String causeMessage = ex.getCause().getMessage().toLowerCase();

            if (causeMessage.contains("duplicate") || causeMessage.contains("unique")) {
                // Tratamento específico para campos únicos
                if (causeMessage.contains("hostname")) {
                    message = "O hostname informado já está sendo usado por outro computador. Cada computador deve ter um hostname único na rede.";
                    error = "Hostname duplicado";
                    details.put("campo", "hostname");
                    details.put("dica", "Escolha um hostname diferente para este equipamento");
                } else if (causeMessage.contains("imei")) {
                    message = "O IMEI informado já está cadastrado para outro celular. Cada IMEI deve ser único.";
                    error = "IMEI duplicado";
                    details.put("campo", "imei");
                    details.put("dica", "Verifique se o IMEI está correto ou se o celular já foi cadastrado");
                } else if (causeMessage.contains("imei2")) {
                    message = "O IMEI2 informado já está cadastrado para outro celular. Cada IMEI2 deve ser único.";
                    error = "IMEI2 duplicado";
                    details.put("campo", "imei2");
                    details.put("dica", "Verifique se o IMEI2 está correto ou se o celular já foi cadastrado");
                } else if (causeMessage.contains("iccid")) {
                    message = "O ICCID informado já está cadastrado para outro chip. Cada ICCID deve ser único.";
                    error = "ICCID duplicado";
                    details.put("campo", "iccid");
                    details.put("dica", "Verifique se o ICCID está correto ou se o chip já foi cadastrado");
                } else if (causeMessage.contains("eid")) {
                    message = "O EID informado já está cadastrado para outro celular. Cada EID deve ser único.";
                    error = "EID duplicado";
                    details.put("campo", "eid");
                    details.put("dica", "Verifique se o EID está correto ou se o eSIM já foi cadastrado");
                } else if (causeMessage.contains("numero_patrimonio")) {
                    message = "O número de patrimônio informado já está cadastrado para outro equipamento. Cada equipamento deve ter um número de patrimônio único.";
                    error = "Número de patrimônio duplicado";
                    details.put("campo", "numeroPatrimonio");
                    details.put("dica", "Use um número de patrimônio diferente para este equipamento");
                } else if (causeMessage.contains("serial_number")) {
                    message = "O número de série informado já está cadastrado para outro equipamento. Cada equipamento deve ter um número de série único.";
                    error = "Número de série duplicado";
                    details.put("campo", "serialNumber");
                    details.put("dica", "Verifique se o número de série está correto ou se o equipamento já foi cadastrado");
                } else if (causeMessage.contains("endereco_ip")) {
                    message = "O endereço IP informado já está sendo usado por outra impressora. Cada impressora deve ter um IP único na rede.";
                    error = "Endereço IP duplicado";
                    details.put("campo", "enderecoIP");
                    details.put("dica", "Configure um endereço IP diferente para esta impressora");
                } else {
                    message = "Dados duplicados: este registro já existe no sistema";
                    error = "Dados duplicados";
                    details.put("dica", "Verifique se o registro já foi cadastrado anteriormente");
                }
            } else if (causeMessage.contains("foreign key")) {
                message = "Erro de referência: registro relacionado não encontrado";
                error = "Referência inválida";
                if (causeMessage.contains("empresa_id")) {
                    message = "A empresa informada não existe no sistema";
                    details.put("campo", "empresaId");
                    details.put("dica", "Verifique se o ID da empresa está correto");
                } else if (causeMessage.contains("departamento_id")) {
                    message = "O departamento informado não existe no sistema";
                    details.put("campo", "departamentoId");
                    details.put("dica", "Verifique se o ID do departamento está correto");
                }
            } else if (causeMessage.contains("not null")) {
                message = "Campo obrigatório não pode ser nulo";
                error = "Campo obrigatório ausente";
                details.put("dica", "Verifique se todos os campos obrigatórios foram preenchidos");
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                error,
                message,
                request.getRequestURI()
        );

        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String message = "Formato JSON inválido ou dados malformados";
        String error = "Erro de formato";
        Map<String, Object> details = new HashMap<>();

        // Tratamento específico para erros de ENUM
        if (ex.getCause() instanceof InvalidFormatException invalidFormatEx) {
            Class<?> targetType = invalidFormatEx.getTargetType();
            Object value = invalidFormatEx.getValue();

            if (targetType != null && targetType.isEnum()) {
                String fieldName = extractFieldNameFromPath(invalidFormatEx.getPath());
                String enumName = targetType.getSimpleName();

                // Mensagens específicas para cada enum
                switch (enumName) {
                    case "EstadoConservacao" -> {
                        message = String.format("Estado de conservação '%s' é inválido. Valores aceitos: NOVO, REGULAR, DANIFICADO", value);
                        error = "Estado de conservação inválido";
                        details.put("campo", fieldName);
                        details.put("valorInvalido", value);
                        details.put("valoresAceitos", List.of("NOVO", "REGULAR", "DANIFICADO"));
                    }
                    case "StatusEquipamento" -> {
                        message = String.format("Status do equipamento '%s' é inválido. Valores aceitos: DISPONIVEL, EM_USO, EM_MANUTENCAO, DANIFICADO, CRIPTOGRAFADO, DESCARTADO", value);
                        error = "Status do equipamento inválido";
                        details.put("campo", fieldName);
                        details.put("valorInvalido", value);
                        details.put("valoresAceitos", List.of("DISPONIVEL", "EM_USO", "EM_MANUTENCAO", "DANIFICADO", "CRIPTOGRAFADO", "DESCARTADO"));
                    }
                    case "StatusPropriedade" -> {
                        message = String.format("Status de propriedade '%s' é inválido. Valores aceitos: PROPRIO, LOCADO", value);
                        error = "Status de propriedade inválido";
                        details.put("campo", fieldName);
                        details.put("valorInvalido", value);
                        details.put("valoresAceitos", List.of("PROPRIO", "LOCADO"));
                    }
                    case "TipoEquipamento" -> {
                        message = String.format("Tipo de equipamento '%s' é inválido. Valores aceitos: NOTEBOOK, DESKTOP, CELULAR, CHIP, IMPRESSORA, MONITOR", value);
                        error = "Tipo de equipamento inválido";
                        details.put("campo", fieldName);
                        details.put("valorInvalido", value);
                        details.put("valoresAceitos", List.of("NOTEBOOK", "DESKTOP", "CELULAR", "CHIP", "IMPRESSORA", "MONITOR"));
                    }
                    default -> {
                        message = String.format("Valor '%s' é inválido para o campo '%s'", value, fieldName);
                        error = "Valor de enum inválido";
                        details.put("campo", fieldName);
                        details.put("valorInvalido", value);
                        details.put("tipoEsperado", enumName);
                    }
                }

                details.put("dica", "Verifique se o valor está escrito corretamente e em maiúsculas");
            } else {
                message = String.format("Valor '%s' não pode ser convertido para o tipo esperado", value);
                details.put("valorInvalido", value);
                details.put("dica", "Verifique se o tipo do dado está correto");
            }
        } else {
            // Outros erros de parsing JSON
            if (ex.getCause() != null) {
                String causeMessage = ex.getCause().getMessage();
                if (causeMessage != null) {
                    if (causeMessage.contains("Unexpected character")) {
                        message = "Caractere inesperado no JSON";
                        details.put("dica", "Verifique se o JSON está bem formado");
                    } else if (causeMessage.contains("missing property")) {
                        message = "Propriedade obrigatória ausente";
                        details.put("dica", "Verifique se todos os campos obrigatórios estão presentes");
                    } else {
                        message = "Erro ao processar JSON: " + extractFieldFromMessage(causeMessage);
                    }
                }
            }
            details.put("dica", "Verifique se o JSON está bem formado e se todos os campos obrigatórios estão presentes");
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                error,
                message,
                request.getRequestURI()
        );

        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message = String.format("Parâmetro '%s' deve ser do tipo %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido");

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de tipo de parâmetro",
                message,
                request.getRequestURI()
        );

        Map<String, Object> details = new HashMap<>();
        details.put("parametro", ex.getName());
        details.put("valorRecebido", ex.getValue());
        details.put("tipoEsperado", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido");
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        String message = String.format("Parâmetro obrigatório '%s' está ausente", ex.getParameterName());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parâmetro ausente",
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Dados duplicados",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação customizada",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Endpoint não encontrado",
                String.format("O endpoint '%s' não foi encontrado", request.getRequestURI()),
                request.getRequestURI()
        );

        Map<String, Object> details = new HashMap<>();
        details.put("metodoHttp", request.getMethod());
        details.put("dica", "Verifique se a URL está correta e se o método HTTP é o esperado");
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request.getRequestURI()
        );

        // Em desenvolvimento, pode ser útil incluir mais detalhes
        Map<String, Object> details = new HashMap<>();
        details.put("tipoErro", ex.getClass().getSimpleName());
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractFieldFromMessage(String message) {
        // Tenta extrair o nome do campo da mensagem de erro
        if (message.contains("field \"")) {
            int start = message.indexOf("field \"") + 7;
            int end = message.indexOf("\"", start);
            if (end > start) {
                return "campo '" + message.substring(start, end) + "' é inválido";
            }
        }
        return "verifique os dados enviados";
    }

    private String extractFieldNameFromPath(List<com.fasterxml.jackson.databind.JsonMappingException.Reference> path) {
        if (path != null && !path.isEmpty()) {
            var lastRef = path.get(path.size() - 1);
            return lastRef.getFieldName();
        }
        return "desconhecido";
    }
}
