package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberCommand;
import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.get.GetCastMemberByIdUseCase;
import com.fullcycle.catalogo.admin.application.castmember.update.UpdateCastMemberCommand;
import com.fullcycle.catalogo.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.catalogo.admin.infrastructure.api.CastMemberAPI;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CastMemberResponse;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.fullcycle.catalogo.admin.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;

    public CastMemberController(
        final CreateCastMemberUseCase createCastMemberUseCase,
        final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
        final UpdateCastMemberUseCase updateCastMemberUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());
        final var output = createCastMemberUseCase.execute(aCommand);
        return ResponseEntity
                .created(URI.create("/cast_members/%s".formatted(output.id())))
                .body(output);
    }

    @Override
    public ResponseEntity<CastMemberResponse> getById(final String id) {
        return ResponseEntity.ok(CastMemberPresenter.present(getCastMemberByIdUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
        final var aCommand = UpdateCastMemberCommand.with(id, aBody.name(), aBody.type());
        return ResponseEntity.ok(updateCastMemberUseCase.execute(aCommand));
    }
}
