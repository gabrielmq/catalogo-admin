package com.fullcycle.catalogo.admin.infrastructure.video;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.video.Video;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.VideoPreview;
import com.fullcycle.catalogo.admin.domain.video.query.VideoSearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.utils.SQLUtils;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.VideoJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.fullcycle.catalogo.admin.domain.utils.CollectionUtils.mapTo;
import static com.fullcycle.catalogo.admin.domain.utils.CollectionUtils.nullIfEmpty;

public class DefaultVideoGateway implements VideoGateway {
    private final VideoRepository repository;

    public DefaultVideoGateway(final VideoRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var aVideoId = anId.getValue();
        if (repository.existsById(aVideoId)) {
            repository.deleteById(aVideoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anId) {
        return repository.findById(anId.getValue()).map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var pageResult = repository.findAll(
            SQLUtils.like(aQuery.terms()),
            nullIfEmpty(mapTo(aQuery.castMembers(), CastMemberID::getValue)),
            nullIfEmpty(mapTo(aQuery.categories(), CategoryID::getValue)),
            nullIfEmpty(mapTo(aQuery.genres(), GenreID::getValue)),
            page
        );
        return new Pagination<>(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.toList()
        );
    }

    private Video save(final Video aVideo) {
        return repository.save(VideoJpaEntity.from(aVideo)).toAggregate();
    }
}
