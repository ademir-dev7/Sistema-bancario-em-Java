package system.bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Modelo da conta bancária
class Conta {

    private final String nome;
    private double saldo;

    public Conta(String nome, double saldoInicial) {
        this.nome = nome;
        this.saldo = saldoInicial;
    }

    // Adiciona valor ao saldo
    public void depositar(double valor) {
        saldo += valor;
    }

    // Retorna false se saldo insuficiente, true se saque OK
    public boolean sacar(double valor) {
        if (valor > saldo) return false;
        saldo -= valor;
        return true;
    }

    public double getSaldo() { return saldo; }
    public String getNome()  { return nome;  }
}

// Gerencia a conta
class Banco {

    private Conta conta;

    public void criarConta(String nome, double saldoInicial) {
        conta = new Conta(nome, saldoInicial);
    }

    public Conta getConta() { return conta; }
}

// Tela principal da aplicação
public class TelaPrincipal extends JFrame {

    private final Banco banco = new Banco();
    private final JTextField campoNome  = new JTextField();
    private final JTextField campoValor = new JTextField();
    private final JTextArea areaResultado = new JTextArea();

    public TelaPrincipal() {
        setTitle("Sistema Bancário");
        setSize(400, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1));

        add(new JLabel("Nome:"));
        add(campoNome);
        add(new JLabel("Valor:"));
        add(campoValor);

        JButton btnCriar     = new JButton("Criar Conta");
        JButton btnDepositar = new JButton("Depositar");
        JButton btnSacar     = new JButton("Sacar");
        JButton btnSaldo     = new JButton("Ver Saldo");

        add(btnCriar);
        add(btnDepositar);
        add(btnSacar);
        add(btnSaldo);

        areaResultado.setEditable(false);
        add(areaResultado);

        // Criar conta
        btnCriar.addActionListener((ActionEvent e) -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty()) {
                areaResultado.setText("Digite um nome para a conta!");
                return;
            }
            try {
                double valor = Double.parseDouble(campoValor.getText());
                if (valor < 0) {
                    areaResultado.setText("Saldo inicial não pode ser negativo!");
                    return;
                }
                banco.criarConta(nome, valor);
                areaResultado.setText("Conta criada para " + nome);
            } catch (NumberFormatException ex) {
                areaResultado.setText("Digite um valor numérico válido!");
            }
        });

        // Depositar
        btnDepositar.addActionListener(e -> {
            if (banco.getConta() == null) {
                areaResultado.setText("Crie uma conta primeiro!");
                return;
            }
            try {
                double valor = Double.parseDouble(campoValor.getText());
                if (valor <= 0) {
                    areaResultado.setText("Digite um valor maior que zero!");
                    return;
                }
                banco.getConta().depositar(valor);
                areaResultado.setText("Depósito realizado!");
            } catch (NumberFormatException ex) {
                areaResultado.setText("Digite um valor numérico válido!");
            }
        });

        // Sacar
        btnSacar.addActionListener(e -> {
            if (banco.getConta() == null) {
                areaResultado.setText("Crie uma conta primeiro!");
                return;
            }
            try {
                double valor = Double.parseDouble(campoValor.getText());
                if (valor <= 0) {
                    areaResultado.setText("Digite um valor maior que zero!");
                    return;
                }
                boolean sucesso = banco.getConta().sacar(valor);
                areaResultado.setText(sucesso ? "Saque realizado!" : "Saldo insuficiente!");
            } catch (NumberFormatException ex) {
                areaResultado.setText("Digite um valor numérico válido!");
            }
        });

        // Ver saldo
        btnSaldo.addActionListener(e -> {
            if (banco.getConta() == null) {
                areaResultado.setText("Crie uma conta primeiro!");
                return;
            }
            areaResultado.setText(
                    "Titular: " + banco.getConta().getNome() +
                            "\nSaldo: R$ " + String.format("%.2f", banco.getConta().getSaldo())
            );
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaPrincipal::new);
    }
}