package com.fullcycle.catalogo.admin.application;

import com.fullcycle.catalogo.admin.domain.Identifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) {
        Mockito.reset(getMocks().toArray());
    }

    protected abstract List<Object> getMocks();

    protected Set<String> asString(final Set<? extends Identifier> ids) {
        return ids.stream().map(Identifier::getValue).collect(Collectors.toSet());
    }

    protected List<String> asString(final List<? extends Identifier> ids) {
        return ids.stream().map(Identifier::getValue).toList();
    }
}
