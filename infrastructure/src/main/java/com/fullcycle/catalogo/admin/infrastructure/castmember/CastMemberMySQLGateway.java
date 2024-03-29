package com.fullcycle.catalogo.admin.infrastructure.castmember;

import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.castmember.persistence.CastMemberRepository;
import com.fullcycle.catalogo.admin.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {
    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(final CastMemberRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public CastMember create(final CastMember aMember) {
        return save(aMember);
    }

    @Override
    public void deleteById(final CastMemberID aMemberId) {
        final var anId = aMemberId.getValue();
        if (repository.existsById(anId)) {
            repository.deleteById(anId);
        }
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID anId) {
        return repository.findById(anId.getValue()).map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(final CastMember aMember) {
        return save(aMember);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(term -> !term.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = repository.findAll(where, page);
        return new Pagination<>(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CastMemberID> existsByIds(final Iterable<CastMemberID> memberIds) {
        final var ids = StreamSupport.stream(memberIds.spliterator(), false)
                .map(CastMemberID::getValue)
                .toList();

        return repository.existsByIds(ids).stream()
                .map(CastMemberID::from)
                .toList();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    private CastMember save(final CastMember aMember) {
        return repository.save(CastMemberJpaEntity.from(aMember)).toAggregate();
    }
}
