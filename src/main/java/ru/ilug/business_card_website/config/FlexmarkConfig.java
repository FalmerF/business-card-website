package ru.ilug.business_card_website.config;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.misc.Extension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FlexmarkConfig {

    @Bean
    public List<Extension> flexmarkExtensions() {
        return List.of(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create()
        );
    }

    @Bean
    public Parser flexmarkParser() {
        return Parser.builder()
                .extensions(flexmarkExtensions())
                .build();
    }

}
