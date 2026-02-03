package ru.ilug.business_card_website.domain.markdown;

import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class CodeBlockRender {

    private final NodeRendererContext context;
    private final HtmlWriter html;
    private final Map<String, String> parameters;

    public void render() {
        html.line()
                .withAttr()
                .attr("class", "code-block")
                .tag("div")
                .line();

        renderHeader();

        context.delegateRender();
        endDivBlock();
    }

    private void renderHeader() {
        html.line()
                .withAttr()
                .attr("class", "code-header")
                .tag("div");

        renderHeaderLeftSide();
        renderRightHeaderSide();
        endDivBlock();
    }

    private void renderHeaderLeftSide() {
        html.line()
                .tag("div")
                .line();

        renderTitleIfExists();
        endDivBlock();
    }

    private void renderRightHeaderSide() {
        html.line()
                .tag("div")
                .line();

        renderLanguageIfExists();
        renderCopyButton();
        endDivBlock();
    }

    private void renderTitleIfExists() {
        String title = parameters.get("title");
        if (title != null) {
            html.line()
                    .tag("div")
                    .text(title)
                    .tag("/div")
                    .line();
        }
    }

    private void renderLanguageIfExists() {
        String language = parameters.get("language");
        if (language != null) {
            html.line()
                    .tag("div")
                    .text(language)
                    .tag("/div")
                    .line();
        }
    }

    private void renderCopyButton() {
        html.line()
                .withAttr()
                .attr("class", "image-button copy-button")
                .attr("src", "/icons/copy.svg")
                .attr("alt", "copy")
                .tag("img")
                .tag("/img")
                .line();
    }

    private void endDivBlock() {
        html.line()
                .tag("/div")
                .line();
    }

}
