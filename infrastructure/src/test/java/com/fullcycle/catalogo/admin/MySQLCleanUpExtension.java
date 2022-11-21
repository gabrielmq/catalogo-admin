package com.fullcycle.catalogo.admin;

import com.fullcycle.catalogo.admin.infrastructure.castmember.persistence.CastMemberRepository;
import com.fullcycle.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.catalogo.admin.infrastructure.genre.persistence.GenreRepository;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
            appContext.getBean(VideoRepository.class),
            appContext.getBean(CastMemberRepository.class),
            appContext.getBean(GenreRepository.class),
            appContext.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}