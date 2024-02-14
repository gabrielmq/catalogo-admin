package com.fullcycle.catalogo.admin.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.get.DefaultGetCastMemberByIdUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.get.GetCastMemberByIdUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.list.DefaultListCastMembersUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.list.ListCastMembersUseCase;
import com.fullcycle.catalogo.admin.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class CastMemberUseCaseConfiguration {
    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfiguration(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}
