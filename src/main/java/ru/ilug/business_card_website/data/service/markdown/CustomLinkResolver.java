package ru.ilug.business_card_website.data.service.markdown;

import com.vladsch.flexmark.html.LinkResolver;
import com.vladsch.flexmark.html.renderer.LinkResolverBasicContext;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.util.ast.Node;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class CustomLinkResolver implements LinkResolver {

    private final String baseUrl;

    @NotNull
    @Override
    public ResolvedLink resolveLink(@NotNull Node node, @NotNull LinkResolverBasicContext context, @NotNull ResolvedLink link) {
        String url = link.getUrl();

        if (url.startsWith("./")) {
            url = url.substring(2);
            return link.withUrl(baseUrl + url);
        }

        return link;
    }
}
