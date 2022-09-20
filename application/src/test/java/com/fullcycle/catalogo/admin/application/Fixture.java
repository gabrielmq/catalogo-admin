package com.fullcycle.catalogo.admin.application;

import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;
import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.genre.Genre;
import com.fullcycle.catalogo.admin.domain.video.rating.Rating;
import com.fullcycle.catalogo.admin.domain.video.media.resource.Resource;
import com.fullcycle.catalogo.admin.domain.video.media.resource.Type;
import com.github.javafaker.Faker;

import static com.fullcycle.catalogo.admin.domain.castmember.CastMemberType.ACTOR;
import static io.vavr.API.*;

public final class Fixture {
    private static final Faker FAKER = new Faker();

    private Fixture() {}

    public static String name() {
        return FAKER.name().fullName();
    }

    public static String title() {
        return FAKER.options().option("Title 1", "Title 2", "Title 3");
    }

    public static int year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static final class Categories {
        private static final Category CATEGORY =
            Category.newCategoryWith("Category", "Category description", true);

        public static Category category() {
            return CATEGORY.clone();
        }
    }

    public static final class Genres {
        private static final Genre GENRE = Genre.newGenreWith("Genre", true);

        public static Genre genre() {
            return Genre.with(GENRE);
        }
    }

    public static final class CastMembers {
        private static final CastMember MEMBER = CastMember.newMember("Member", ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember member() {
            return CastMember.with(MEMBER);
        }
    }

    public static final class Videos {
        public static String description() {
            return FAKER.options().option("Description 1", "Description 2", "Description 3");
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static Resource resource(final Type type) {
            final String contentType = Match(type).of(
                Case($(List(Type.VIDEO, Type.TRAILER)::contains), "video/mp4"),
                Case($(), "image/jpg")
            );

            final byte[] content = "Conteudo".getBytes();
            return Resource.of(content, contentType, type.name().toLowerCase(), type);
        }
    }
}
