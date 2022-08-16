package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberCommand;
import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberOutput;
import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.catalogo.admin.infrastructure.api.CastMemberAPI;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
    private final CreateCastMemberUseCase createCastMemberUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());
        final var output = createCastMemberUseCase.execute(aCommand);
        return ResponseEntity
                .created(URI.create("/cast_members/%s".formatted(output.id())))
                .body(output);
    }
}
