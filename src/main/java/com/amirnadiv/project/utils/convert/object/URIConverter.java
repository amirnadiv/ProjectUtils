package com.amirnadiv.project.utils.common.convert.object;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class URIConverter extends ObjectConverter<URI> implements TypeConverter<URI> {

    public URIConverter() {
        register(URI.class);
    }

    @Override
    public URI toConvert(String value) {
        return convert(value);
    }

    @Override
    public String fromConvert(URI value) {
        return String.valueOf(value);
    }

    public URI toConvert(Object value) {
        if (value instanceof URI) {
            return (URI) value;
        }

        if (value instanceof File) {
            File file = (File) value;
            return file.toURI();
        }

        if (value instanceof URL) {
            URL url = (URL) value;
            try {
                return url.toURI();
            } catch (URISyntaxException e) {
                throw new ConvertException(value, e);
            }
        }

        return convert(value.toString());
    }

    private URI convert(String value) {
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new ConvertException(value, e);
        }
    }

}
