package com.insurance.txo.view.txoentry;

import com.insurance.txo.entity.TxoEntry;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "txo-entries/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "txo_TxoEntry.detail")
@ViewDescriptor(path = "txo-entry-detail-view.xml")
@EditedEntityContainer("txoEntryDc")
public class TxoEntryDetailView extends StandardDetailView<TxoEntry> {
}