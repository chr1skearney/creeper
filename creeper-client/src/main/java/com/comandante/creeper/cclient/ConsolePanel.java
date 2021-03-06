package com.comandante.creeper.cclient;

import com.comandante.creeper.events.PlayerData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.terminal.TtyConnector;
import com.terminal.emulator.JediEmulator;
import com.terminal.ui.JediTermWidget;
import com.terminal.ui.ResetEvent;
import com.terminal.ui.TerminalSession;
import com.terminal.ui.TerminalWidget;
import com.terminal.ui.settings.DefaultTabbedSettingsProvider;
import com.terminal.ui.settings.TabbedSettingsProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class ConsolePanel extends JPanel {

    private final JediTermWidget jediTermWidget;
    private final List<JediEmulator.NonControlCharListener> nonControlCharListeners;
    private final Supplier<TtyConnector> ttyConnectorSupplier;
    private final Input input;
    private final TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Console");

    private final static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ConsolePanel.class);

    public ConsolePanel(ConsoleStatusBar consoleStatusBar,
                        MapPanel.MapWindowMovementHandler mapWindowMovementHandler,
                        List<JediEmulator.NonControlCharListener> nonControlCharListeners,
                        Supplier<TtyConnector> ttyConnectorSupplier
    ) {
        this.nonControlCharListeners = nonControlCharListeners;
        this.jediTermWidget = createTerminalWidget(new DefaultTabbedSettingsProvider(), nonControlCharListeners);
        this.ttyConnectorSupplier = ttyConnectorSupplier;

        jediTermWidget.getTerminalDisplay().setCursorVisible(false);
        jediTermWidget.getTerminalDisplay().setScrollingEnabled(true);

        consoleStatusBar.setBackground(Color.darkGray);
        JPanel inputAndStatus = new JPanel(new BorderLayout());
        this.input = new Input(line -> {
            try {
                jediTermWidget.getCurrentSession().getTtyConnector().write(line + "\n");
                jediTermWidget.getMyScrollBar().setValue(jediTermWidget.getMyScrollBar().getMaximum());
            } catch (IOException e) {
                LOG.error("Unable to write to terminal!", e);
            }
        }, mapWindowMovementHandler);


        inputAndStatus.add(input, BorderLayout.PAGE_END);
        inputAndStatus.add(consoleStatusBar, BorderLayout.PAGE_START);

        //this.border.setTitleColor(Color.white);
        this.setBackground(Color.black);
        this.setBorder(border);
        this.setLayout(new BorderLayout());
        this.add(jediTermWidget.getComponent(), BorderLayout.CENTER);
        this.add(inputAndStatus, BorderLayout.PAGE_END);
        this.setFocusable(false);


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                getInput().getField().requestFocus();
            }
        });

        this.setVisible(true);
        this.openSession(jediTermWidget);
    }


    protected JediTermWidget createTerminalWidget(@NotNull TabbedSettingsProvider settingsProvider, List<JediEmulator.NonControlCharListener> nonControlCharListeners) {
        return new JediTermWidget(settingsProvider, nonControlCharListeners);
    }

    protected void openSession(TerminalWidget terminal) {
        if (terminal.canOpenSession()) {
            openSession(terminal, ttyConnectorSupplier.get());
        }
    }

    public void openSession(TerminalWidget terminal, TtyConnector ttyConnector) {
        TerminalSession session = terminal.createTerminalSession(ttyConnector);
        session.start();
    }

    public void connect() {
        this.openSession(jediTermWidget);
    }

    private void reset() {
        try {
            jediTermWidget.getCurrentSession().getTtyConnector().close();
            jediTermWidget.getCurrentSession().getTerminal().writeCharacters("\n" + "Disconnected." + "\n");
        } catch (Exception e) {
            LOG.error("Failed to write to terminal!", e);
        }

    }

    public JediTermWidget getJediTermWidget() {
        return jediTermWidget;
    }

    public Input getInput() {
        return input;
    }

    @Subscribe
    public void resetEvent(ResetEvent resetEvent) {
        reset();
    }
}
