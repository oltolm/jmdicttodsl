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
public class Sense {
    public final List<String> pos = new ArrayList<>();
    public final List<String> field = new ArrayList<>();
    public final List<String> misc = new ArrayList<>();
    public final List<String> dial = new ArrayList<>();
    public final List<LSource> lsource = new ArrayList<>();
    public final List<String> gloss = new ArrayList<>();
    public final List<String> xref = new ArrayList<>();
    public final List<String> stagk = new ArrayList<>();
    public final List<String> stagr = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Sense other = (Sense) obj;
        if (this.stagk != other.stagk && (this.stagk == null || !this.stagk.equals(other.stagk)))
            return false;
        if (this.stagr != other.stagr && (this.stagr == null || !this.stagr.equals(other.stagr)))
            return false;
        if (this.pos != other.pos && (this.pos == null || !this.pos.equals(other.pos)))
            return false;
        if (this.xref != other.xref && (this.xref == null || !this.xref.equals(other.xref)))
            return false;
        if (this.field != other.field && (this.field == null || !this.field.equals(other.field)))
            return false;
        if (this.misc != other.misc && (this.misc == null || !this.misc.equals(other.misc)))
            return false;
        if (this.lsource != other.lsource && (this.lsource == null || !this.lsource.equals(other.lsource)))
            return false;
        if (this.dial != other.dial && (this.dial == null || !this.dial.equals(other.dial)))
            return false;
        if (this.gloss != other.gloss && (this.gloss == null || !this.gloss.equals(other.gloss)))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public String toString() {
        return "Sense{" + "pos=" + pos + ", field=" + field + ", misc=" + misc + ", dial=" + dial + ", lsource=" + lsource + ", gloss=" + gloss + ", xref=" + xref + ", stagk=" + stagk + ", stagr=" + stagr + '}';
    }
}
