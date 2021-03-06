/*******************************************************************************
 * Copyright (c) 2011 Robert Munteanu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Robert Munteanu - initial API and implementation
 *******************************************************************************/
package org.review_board.ereviewboard.subclipse.internal.wizards;

import java.util.Collections;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.ui.TasksUi;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.review_board.ereviewboard.core.model.ReviewRequest;
import org.review_board.ereviewboard.ui.internal.control.EnhancedAutoCompleteField;
import org.review_board.ereviewboard.ui.internal.control.Proposal;
import org.review_board.ereviewboard.ui.util.UiUtils;

/**
 * @author Robert Munteanu
 *
 */
class PublishReviewRequestPage extends WizardPage {

    private EnhancedAutoCompleteField _toUserComboAutoCompleteField;
    private EnhancedAutoCompleteField _toGroupComboAutoCompleteField;
    private final ReviewRequest reviewRequest = new ReviewRequest();
    
    private final CreateReviewRequestWizardContext _context;

    public PublishReviewRequestPage(CreateReviewRequestWizardContext context) {

        super("Publish review request", "Publish review request", null);
        
        setMessage("Fill in the review request details. Description, summary and a target person or a target group are required.", IMessageProvider.INFORMATION);
        
        _context = context;
    }

    public void createControl(Composite parent) {

        Composite layout = new Composite(parent, SWT.NONE);
        
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(layout);
        
        newLabel(layout, "Summary:");
        
        final StyledText summary = UiUtils.newSinglelineText(layout);
        summary.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
            
               reviewRequest.setSummary(summary.getText());
               
               getContainer().updateButtons();
            }
        });

        newLabel(layout, "Bugs closed:");
        
        final Text bugsClosed = newText(layout);
        ITask activeTask = TasksUi.getTaskActivityManager().getActiveTask();
        if ( activeTask != null && activeTask.getTaskKey() != null)
            bugsClosed.setText(activeTask.getTaskKey());
        
        bugsClosed.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                
                reviewRequest.setBugsClosed(Collections.singletonList(bugsClosed.getText()));
                
                getContainer().updateButtons();
            }
        });
        
        newLabel(layout, "Branch:");
        
        final Text branch = newText(layout);
        branch.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                
                reviewRequest.setBranch(branch.getText());
                
                getContainer().updateButtons();
            }
        });
        
        newLabel(layout, "Description:");
        
        final StyledText description = UiUtils.newMultilineText(layout);
        
        description.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
            
               reviewRequest.setDescription(description.getText());
               
               getContainer().updateButtons();
            }
        });

        newLabel(layout, "Testing done:");
        
        final StyledText testingDone = UiUtils.newMultilineText(layout);
        
        testingDone.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
            
                reviewRequest.setTestingDone(testingDone.getText());
                
                getContainer().updateButtons();
            }
        });
        
        newLabel(layout, "Target user:");
        
        final Text toUserText = newText(layout);
        
        _toUserComboAutoCompleteField = new EnhancedAutoCompleteField(toUserText, new Proposal[0]);
        
        toUserText.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
            
                reviewRequest.setTargetPeople(Collections.singletonList(toUserText.getText()));
                
                getContainer().updateButtons();
            }
        });
        
        newLabel(layout, "Target group:");
        
        final Text toGroupText = newText(layout);
        
        _toGroupComboAutoCompleteField = new EnhancedAutoCompleteField(toGroupText, new Proposal[0]);
        
        toGroupText.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                
                reviewRequest.setTargetGroups(Collections.singletonList(toGroupText.getText()));
                
                getContainer().updateButtons();
            }
        });
        
        setControl(layout);
    }

    private void newLabel(Composite layout, String text) {

        Label descriptionLabel = new Label(layout, SWT.NONE );
        descriptionLabel.setText(text);
        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(descriptionLabel);
    }
    
    private Text newText(Composite layout) {
        
        final Text toUserText = new Text(layout, SWT.BORDER | SWT.SINGLE);
        GridDataFactory.swtDefaults().hint(UiUtils.FULL_TEXT_WIDTH, SWT.DEFAULT).applyTo(toUserText);
        return toUserText;
    }
    
    @Override
    public boolean isPageComplete() {
    
        return super.isPageComplete() && checkValid();
    }
    
    private boolean checkValid() {

        if (reviewRequest.getSummary() == null || reviewRequest.getSummary().length() == 0 ) {
            return false;
        }
        
        if ( reviewRequest.getDescription() == null || reviewRequest.getDescription().length() == 0 ) {
            return false;
        }
        
        if ( reviewRequest.getTargetGroups().isEmpty() && reviewRequest.getTargetPeople().isEmpty()) {
            return false;
        }
        
        return true;
            
    }

    @Override
    public void setVisible(boolean visible) {
    
        if ( visible) {
            _toUserComboAutoCompleteField.setProposals(UiUtils.adaptUsers(_context.getReviewboardClient().getClientData().getUsers()));
            _toGroupComboAutoCompleteField.setProposals(UiUtils.adaptGroups(_context.getReviewboardClient().getClientData().getGroups()));
        }
        
        super.setVisible(visible);
    }

    public ReviewRequest getReviewRequest() {

        return reviewRequest;
    }
}
