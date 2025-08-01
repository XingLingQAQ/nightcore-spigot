package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.decorator.BaseColorDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

@Deprecated
public class HexColorTag extends Tag implements ContentTag {

    public static final String NAME = "color";

    public HexColorTag() {
        super("c", new String[]{"color", "colour"});
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String text, @NotNull String hex) {
        return this.wrap(text, hex);//brackets(this.name + ":" + hex) + text + closedBrackets(this.name);
    }

    @NotNull
    public String open(@NotNull String hex) {
        return TagUtils.brackets(this.name + ParserUtils.DELIMITER + hex);
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull Color color) {
        return this.wrap(string, ParserUtils.colorToHexString(color));
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull String hex) {
        return this.open(hex) + string + TagUtils.closedBrackets(this.name);
    }

    @Override
    @Nullable
    public BaseColorDecorator parse(@NotNull String tagContent) {
        Color color = ParserUtils.colorFromSchemeOrHex(tagContent);
        if (color == null) return null;

        return new BaseColorDecorator(color);
    }
}
