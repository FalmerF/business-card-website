package ru.ilug.business_card_website.domain.markdown.code_block;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeBlockRenderInitializer implements NodeRenderer {

    private static final Pattern LANGUAGE_PATTERN = Pattern.compile("^(\\S+)");
    private static final Pattern PARAMTER_PATTERN = Pattern.compile("\\s(\\w+)=\"((?:\\\\.|[^\"\\\\])*)\"");

    private Map<String, String> parameters;

    @Override
    public @Nullable Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        return Set.of(new NodeRenderingHandler<>(FencedCodeBlock.class, this::initializeRender));
    }

    private void initializeRender(FencedCodeBlock codeBlock, NodeRendererContext context, HtmlWriter html) {
        extractParametersFromCodeBlock(codeBlock);
        CodeBlockRender codeBlockRender = new CodeBlockRender(context, html, parameters);
        codeBlockRender.render();
    }

    private void extractParametersFromCodeBlock(FencedCodeBlock codeBlock) {
        parameters = new HashMap<>();
        String info = codeBlock.getInfo().toString();

        if (info == null || info.isEmpty()) {
            return;
        }

        extractParametersFromInfo(info);
    }

    private void extractParametersFromInfo(String info) {
        extractAndPutLanguageParameter(info);
        extractAndPutOtherParameters(info);
    }

    private void extractAndPutLanguageParameter(String info) {
        Matcher matcher = LANGUAGE_PATTERN.matcher(info);
        if (matcher.find()) {
            String language = matcher.group(1);
            parameters.put("language", language);
        }
    }

    private void extractAndPutOtherParameters(String info) {
        Matcher matcher = PARAMTER_PATTERN.matcher(info);
        while (matcher.find()) {
            String parameterName = matcher.group(1);
            String parameterValue = matcher.group(2);
            parameters.put(parameterName, parameterValue);
        }
    }
}
