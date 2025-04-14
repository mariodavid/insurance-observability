package com.insurance.txo.view.txoentry;

import com.insurance.txo.entity.TxoEntry;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


@Route(value = "txo-entries", layout = DefaultMainViewParent.class)
@ViewController(id = "txo_TxoEntry.list")
@ViewDescriptor(path = "txo-entry-list-view.xml")
@LookupComponent("txoEntriesDataGrid")
@DialogMode(width = "64em")
public class TxoEntryListView extends StandardListView<TxoEntry> {
}