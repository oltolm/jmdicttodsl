/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmdicttodsl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oleg Tolmatcev
 */
class Entry {

    public String kana;
    public String kanji;
    public final List<String> info = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Entry other = (Entry) obj;
        if (this.info != other.info && (this.info == null || !this.info.equals(other.info)))
            return false;
        if ((this.kanji == null) ? (other.kanji != null) : !this.kanji.equals(other.kanji))
            return false;
        if ((this.kana == null) ? (other.kana != null) : !this.kana.equals(other.kana))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public String toString() {
        return "Entry{" + "kana=" + kana + ", kanji=" + kanji + ", info=" + info + '}';
    }
}
