/*******************************************************************************
 * Copyright (c) 2004, 2011 Robert Munteanu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Robert Munteanu - initial API and implementation
 *******************************************************************************/
package org.review_board.ereviewboard.core.model;

import java.io.Serializable;

/**
 * Domain class for a FileDiff
 * 
 * 
 * @author Robert Munteanu
 * 
 */
public class FileDiff implements Serializable {

    private final int id;
    private final String sourceFile;
    private final String sourceRevision;

    public FileDiff(int id, String sourceFile, String sourceRevision) {
        this.id = id;
        this.sourceFile = sourceFile;
        this.sourceRevision = sourceRevision;
    }

    public int getId() {
        return id;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public String getSourceRevision() {
        return sourceRevision;
    }

}
