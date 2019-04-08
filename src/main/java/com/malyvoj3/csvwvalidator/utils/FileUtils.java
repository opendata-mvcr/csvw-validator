package com.malyvoj3.csvwvalidator.utils;

import com.malyvoj3.csvwvalidator.domain.ContentType;
import com.malyvoj3.csvwvalidator.domain.FileResponse;
import com.malyvoj3.csvwvalidator.domain.Link;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

// TODO predelat na beanu
@UtilityClass
@Slf4j
public class FileUtils {

    public boolean isUtf8(URI uri) {
        boolean isUtf;
        try {
            isUtf = isUtf8(IOUtils.toByteArray(uri));
        } catch (IOException e) {
            isUtf = false;
        }
        return isUtf;
    }

    public boolean isUtf8(byte[] array) {
        CharsetDecoder decoder =
                StandardCharsets.UTF_8.newDecoder();
        try {
            decoder.decode(
                    ByteBuffer.wrap(array));
        } catch (CharacterCodingException ex) {
           return false;
        }
        return true;
    }

    public FileResponse downloadFile(String stringUrl) {
        FileResponse fileResponse;
        // TODO predelat na Strategy pattern.
        if (isFileUrl(stringUrl)) {
            fileResponse = getLocalFile(stringUrl);
        } else {
            fileResponse = downloadRemoteFile(stringUrl);
        }
        return fileResponse;
    }

    private static FileResponse getLocalFile(@NonNull String stringUrl) {
        FileResponse fileResponse = null;
        String normalizedUrl = UriUtils.normalizeUri(stringUrl);
        URL url;
        try {
            url = new URL(normalizedUrl);
            log.info("Opening local file {}.", normalizedUrl);
            byte[] byteArray = IOUtils.toByteArray(url);
            fileResponse = new FileResponse();
            fileResponse.setContent(byteArray);
            fileResponse.setUrl(normalizedUrl);
            fileResponse.setRemoteFile(false);
        } catch (IOException e) {
            log.error("Cannot locate local file with url {}.", stringUrl);
        }
        return fileResponse;
    }

    private FileResponse downloadRemoteFile(@NonNull String stringUrl) {
        String normalizedUrl = UriUtils.normalizeUri(stringUrl);
        URL url;
        FileResponse fileResponse = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(normalizedUrl);
            connection = (HttpURLConnection) url.openConnection();
            log.info("Downloading file {}.", normalizedUrl);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("GET");
            log.info("{} responded with response code {}.", normalizedUrl, connection.getResponseCode());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                normalizedUrl = UriUtils.normalizeUri(connection.getHeaderField("Location"));
                url = new URL(normalizedUrl);
                connection.disconnect();
                connection = (HttpURLConnection) url.openConnection();
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    fileResponse = new FileResponse();
                    fileResponse.setResponseCode(connection.getResponseCode());
                    fileResponse.setUrl(normalizedUrl);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        fileResponse.setContent(IOUtils.toByteArray(inputStream));
                        fileResponse.setContentType(createContentType(connection.getContentType()));
                        fileResponse.setLink(createLink(connection.getHeaderFields().get("Link"), normalizedUrl));
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Cannot locate local file with url {}.", stringUrl);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return fileResponse;
    }

    private boolean isFileUrl(String url) {
        return url != null && url.startsWith("file:/");
    }

    private ContentType createContentType(String contentTypeHeader) {
        ContentType contentType = new ContentType();
        contentType.setType(getHeaderValue(contentTypeHeader));
        contentType.setCharset(getHeaderParameter(contentTypeHeader, "charset"));
        contentType.setHeader(getHeaderParameter(contentTypeHeader, "header"));
        return contentType;
    }

    private Link createLink(List<String> linkHeaders, String fileUrl) {
        Link link = null;
        String linkHeader = null;
        if (linkHeaders != null && linkHeaders.size() > 0) {
            linkHeader = linkHeaders.get(linkHeaders.size() - 1);
            Link tmp = new Link();
            String url = Optional.ofNullable(getHeaderValue(linkHeader))
                    .map(FileUtils::removeLinkBrackets)
                    .orElse(null);
            tmp.setLink(UriUtils.resolveUri(fileUrl, url));
            tmp.setRel(getHeaderParameter(linkHeader, "rel"));
            tmp.setType(getHeaderParameter(linkHeader, "type"));
            if ("describedby".equals(tmp.getRel())) {
                link = tmp;
            }
        }
        return link;
    }

    private String removeLinkBrackets(String link) {
        String result = link;
        if (result != null) {
            if (result.startsWith("<")) {
                result = result.substring(1);
            }
            if (result.endsWith(">")) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    private String getHeaderValue(String header) {
        String value = null;
        if (StringUtils.isNotEmpty(header)) {
            int index = StringUtils.indexOf(header, ";");
            if (index > 0) {
                value = header.substring(0, index);
            } else {
                value = header;
            }
        }
        return value;
    }

    private String getHeaderParameter(String header, @NonNull String parameterName) {
        String value = null;
        if (StringUtils.isNotEmpty(header)) {
            int index = StringUtils.indexOfIgnoreCase(header, parameterName + "=");
            if (index > 0) {
                int startingIndex = index + parameterName.length() + 1;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = startingIndex; i < header.length(); i++) {
                    if (header.charAt(i) == ';') {
                        break;
                    }
                    stringBuilder.append(header.charAt(i));
                }
                value = stringBuilder.toString();
            }
        }
        return value;
    }

}
