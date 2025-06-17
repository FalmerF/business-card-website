package ru.ilug.business_card_website.link;

import com.vladsch.flexmark.html.LinkResolver;
import com.vladsch.flexmark.html.LinkResolverFactory;
import com.vladsch.flexmark.html.renderer.LinkResolverBasicContext;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@RequiredArgsConstructor
public class CustomLinkResolverFactory implements LinkResolverFactory {

    private final String baseUrl;

    @Override
    public @Nullable Set<Class<?>> getAfterDependents() {
        return Set.of();
    }

    @Override
    public @Nullable Set<Class<?>> getBeforeDependents() {
        return Set.of();
    }

    @Override
    public boolean affectsGlobalScope() {
        return false;
    }

    @Override
    public @NotNull LinkResolver apply(@NotNull LinkResolverBasicContext context) {
        return new CustomLinkResolver(baseUrl);
    }
}
