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
public class Reading {
    public String reb;
    public final List<String> re_inf = new ArrayList<>();
    public final List<String> re_restr = new ArrayList<>();
    public final List<Sense> sense = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.reb);
        hash = 89 * hash + Objects.hashCode(this.re_inf);
        hash = 89 * hash + Objects.hashCode(this.re_restr);
        hash = 89 * hash + Objects.hashCode(this.sense);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Reading other = (Reading) obj;
        if (!Objects.equals(this.reb, other.reb))
            return false;
        if (!Objects.equals(this.re_inf, other.re_inf))
            return false;
        if (!Objects.equals(this.re_restr, other.re_restr))
            return false;
        if (!Objects.equals(this.sense, other.sense))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Reading{" + "reb=" + reb + ", re_inf=" + re_inf + ", re_restr=" + re_restr + ", sense=" + sense + '}';
    }
}
