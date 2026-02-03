package ru.ilug.business_card_website.domain.markdown.image;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Slf4j
public class ImageRenderInitializer implements NodeRenderer {

    @Override
    public @Nullable Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        return Set.of(new NodeRenderingHandler<>(Image.class, this::initializeRender));
    }

    private void initializeRender(Image image, NodeRendererContext context, HtmlWriter html) {
        String imageText = image.getText().toString();
        ImageRender imageRender = new ImageRender(context, html, imageText);
        imageRender.render();
    }
}
