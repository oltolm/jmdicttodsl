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
import java.util.Objects;

/**
 *
 * @author Oleg Tolmatcev
 */
public class Kanji {
    public String keb;
    public final List<String> ke_inf = new ArrayList<>();
    public final List<Sense> sense = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.keb);
        hash = 17 * hash + Objects.hashCode(this.ke_inf);
        hash = 17 * hash + Objects.hashCode(this.sense);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Kanji other = (Kanji) obj;
        if (!Objects.equals(this.keb, other.keb))
            return false;
        if (!Objects.equals(this.ke_inf, other.ke_inf))
            return false;
        if (!Objects.equals(this.sense, other.sense))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Kanji{" + "keb=" + keb + ", ke_inf=" + ke_inf + ", sense=" + sense + '}';
    }
}
