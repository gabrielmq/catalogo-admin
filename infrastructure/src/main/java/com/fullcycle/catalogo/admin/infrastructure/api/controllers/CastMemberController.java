package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberCommand;
import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.get.GetCastMemberByIdUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.list.ListCastMembersUseCase;
import com.fullcycle.catalogo.admin.application.castmember.update.UpdateCastMemberCommand;
import com.fullcycle.catalogo.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.api.CastMemberAPI;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CastMemberListResponse;
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
    private final ListCastMembersUseCase listCastMembersUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;

    public CastMemberController(
        final CreateCastMemberUseCase createCastMemberUseCase,
        final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
        final ListCastMembersUseCase listCastMembersUseCase,
        final UpdateCastMemberUseCase updateCastMemberUseCase,
        final DeleteCastMemberUseCase deleteCastMemberUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
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
    public Pagination<CastMemberListResponse> list(
        final String search,
        final int page,
        final int perPage,
        final String sort,
        final String direction
    ) {
        final var aQuery = new SearchQuery(page, perPage, search, sort, direction);
        return listCastMembersUseCase.execute(aQuery).map(CastMemberPresenter::present);
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

    @Override
    public void deleteById(final String id) {
        deleteCastMemberUseCase.execute(id);
    }
}
