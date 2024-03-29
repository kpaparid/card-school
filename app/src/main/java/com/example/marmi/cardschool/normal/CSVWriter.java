package com.example.marmi.cardschool.normal;

/**
 * Copyright 2005 Bytecode Pty Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * The code copied from http://opencsv.sourceforge.net/
 *
 * While incorporating into secrets, the following changes were made:
 *
 * - removed the following methods to keep the bytecode smaller:
 *   writeAll(), all methods related to sql
 */

import java.io.PrintWriter;
import java.io.Writer;

/**
 * A very simple CSV writer released under a commercial-friendly license.
 *
 * @author Glen Smith
 *
 */
public class CSVWriter {

    /** The character used for escaping quotes. */
    public static final char DEFAULT_ESCAPE_CHARACTER = '"';
    /** The default separator to use if none is supplied to the constructor. */
    public static final char DEFAULT_SEPARATOR = ';';
    /**
     * The default quote character to use if none is supplied to the
     * constructor.
     */
    public static final char DEFAULT_QUOTE_CHARACTER = '"';
    /** The quote constant to use when you wish to suppress all quoting. */
    public static final char NO_QUOTE_CHARACTER = '\u0000';
    /** The escape constant to use when you wish to suppress all escaping. */
    public static final char NO_ESCAPE_CHARACTER = '\u0000';
    /** Default line terminator uses platform encoding. */
    public static final String DEFAULT_LINE_END = "\n";
    private final PrintWriter pw;
    private final char separator;
    private final char quotechar;
    private final char escapechar;
    private final String lineEnd;

    /**
     * Constructs CSVWriter using a comma for the separator.
     *
     * @param writer
     *            the writer to an underlying CSV source.
     */
    public CSVWriter(Writer writer) {
        this(writer, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER,
                DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
    }

    /**
     * Constructs CSVWriter with supplied separator, quote char, escape char and line ending.
     *
     * @param writer
     *            the writer to an underlying CSV source.
     * @param separator
     *            the delimiter to use for separating entries
     * @param quotechar
     *            the character to use for quoted elements
     * @param escapechar
     *            the character to use for escaping quotechars or escapechars
     * @param lineEnd
     * 			  the line feed terminator to use
     */
    public CSVWriter(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        this.pw = new PrintWriter(writer);
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
    }

    /**
     * Writes the next line to the file.
     *
     * @param nextLine
     *            a string array with each comma-separated element as a separate
     *            entry.
     */
    public void writeNext(String[] nextLine) {

        if (nextLine == null)
            return;

        StringBuffer sb = new StringBuffer();


        sb.append(nextLine[0]);
        sb.append(separator);
        sb.append(nextLine[1]);
        sb.append(separator);
        sb.append(nextLine[2]);
        sb.append(separator);
        sb.append(nextLine[3]);
        sb.append(separator);
        sb.append(nextLine[4]);
        sb.append(separator);
        sb.append(nextLine[5]);
        sb.append(separator);
        sb.append(nextLine[6]);
        sb.append(lineEnd);

        pw.write(sb.toString());

    }

    /**
     * Flush underlying stream to writer.
     *
     */
    public void flush() {

        pw.flush();

    }

    /**
     * Close the underlying stream writer flushing any buffered content.
     *
     *
     */
    public void close() {
        pw.flush();
        pw.close();
    }

}