package ru.ilug.business_card_website.domain.markdown.image;

import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImageRender {

    private final NodeRendererContext context;
    private final HtmlWriter html;
    private final String captionText;

    public void render() {
        startRenderFigure();
        context.delegateRender();
        renderFigcaptionIfExists();
        endRenderFigure();
    }

    private void startRenderFigure() {
        html.line()
                .tag("figure")
                .line();
    }

    private void renderFigcaptionIfExists() {
        if (captionText != null && !captionText.isEmpty()) {
            html.line()
                    .tag("figcaption")
                    .text(captionText)
                    .tag("/figcaption")
                    .line();
        }
    }

    private void endRenderFigure() {
        html.line()
                .tag("/figure")
                .line();
    }
}
