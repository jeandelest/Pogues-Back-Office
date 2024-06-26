package fr.insee.pogues.configuration.swagger.role;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class DisplayRolesOnSwaggerUI implements OperationCustomizer {
    public static final String AUTHORIZED_ROLES = "Authorized roles: ";

    /**
     * Display roles allowed to use an endpoint in the description field
     * @param operation spring doc operation endpoint
     * @param handlerMethod method handler used to retrieve annotations
     * @return the operation with role description for UI
     */
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var preAuthorizeAnnotation = handlerMethod.getMethodAnnotation(PreAuthorize.class);
        StringBuilder description = new StringBuilder();
        if(preAuthorizeAnnotation == null) {
            return operation;
        }
        if(operation.getDescription() != null) {
            description
                    .append(operation.getDescription())
                    .append("\n");
        }
        description.append(AUTHORIZED_ROLES);
        String roles = preAuthorizeAnnotation.value();
        for(RoleUIMapper roleUIMapper : RoleUIMapper.values()) {
            if(roles.contains(roleUIMapper.getRoleExpression())) {
                description
                        .append(roleUIMapper.name())
                        .append(" / ");
            }
        }
        operation.setDescription(description.toString());
        return operation;
    }
}