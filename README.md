<h1 align="center">🤖 RoboNovembro - FRC </h1>

<p align="center">
  <strong>FIRST Robotics Competition | MTECH #9603</strong>
</p>
Este repositório contém o código do robô, equipado com um sistema de movimentação Swerve Drive, elevador de três níveis e uma garra articulada com diferentes posições para coleta de game pieces.

O desenvolvimento utiliza Java e WPILib, com implementação de controle de posição, gerenciamento de estados e integração entre os diferentes subsistemas do robô.

---

## ⚙️ Sistemas do Robô

### 🏎️ Swerve Drive

O robô utiliza o sistema de movimentação Swerve Drive, permitindo que cada módulo controle individualmente sua direção e velocidade.

Esse sistema proporciona movimentação omnidirecional, permitindo que o robô se desloque em diferentes direções sem precisar alterar necessariamente sua orientação.

- Controle individual dos módulos.
- Movimentação omnidirecional.
- Controle de direção e velocidade.
- Integração com o sistema de condução do robô.

---

### 🏗️ Elevator — Elevador de 3 Níveis

O elevador foi desenvolvido para realizar movimentações entre diferentes posições predefinidas, permitindo o posicionamento do mecanismo em três níveis de operação.

| Nível | Altura |
|---|---:|
| 🟢 L1 | 9,5 cm |
| 🔵 L2 | 52 cm |
| 🟣 L3 | 72 cm |

O sistema utiliza um motor brushless controlado por um SparkMax e um encoder integrado para realizar o acompanhamento da posição do mecanismo.

#### 🎯 Controle de posição

O elevador utiliza controle Closed Loop de posição, permitindo que o motor seja comandado para alcançar posições específicas.

A posição é acompanhada por meio do encoder, com conversão dos valores de rotação para centímetros, facilitando o controle e o monitoramento do mecanismo.

#### 📈 PID e Feedforward

O controle do elevador utiliza configurações de PID e Feedforward para auxiliar no controle de movimentação e no comportamento do mecanismo durante o deslocamento.

Foram configurados parâmetros específicos para os movimentos de subida e descida, permitindo utilizar ajustes diferentes dependendo da direção do movimento.

#### 🔄 Slots de controle — Subida e Descida

O sistema utiliza dois Closed Loop Slots no controlador SparkMax:

- **Slot 0 — Subida:** utilizado quando o elevador recebe um comando para se movimentar para uma posição superior.
- **Slot 1 — Descida:** utilizado quando o elevador recebe um comando para se movimentar para uma posição inferior.

A seleção do slot é realizada automaticamente de acordo com a posição atual do elevador e o setpoint solicitado.

Cada slot possui configurações próprias de PID e Feedforward, permitindo ajustar o comportamento do mecanismo de acordo com a direção do movimento.

#### 🧠 Máquina de estados (State Machine)

O elevador possui uma máquina de estados responsável por acompanhar a situação atual do mecanismo.

Os estados implementados são:

- **Movendo:** indica que o elevador está se deslocando em direção ao setpoint solicitado.
- **Alvo_L1:** indica que o elevador alcançou a posição correspondente ao L1.
- **Alvo_L2:** indica que o elevador alcançou a posição correspondente ao L2.
- **Alvo_L3:** indica que o elevador alcançou a posição correspondente ao L3.
- **PontoZero:** representa o estado associado à posição zero do mecanismo.

A transição entre os estados de movimentação e os estados de destino é realizada por meio da verificação da posição atual do encoder em relação ao setpoint, utilizando uma tolerância de posição.

#### 📊 Monitoramento

O elevador possui informações disponibilizadas no SmartDashboard para acompanhamento e análise durante os testes.

Os dados monitorados incluem:

- Posição atual do elevador.
- Velocidade do encoder.
- Tensão aplicada ao motor.
- Output aplicado pelo controlador.
- Erro entre posição atual e setpoint.
- Slot de controle atualmente utilizado.
- Estado atual do elevador.

Essas informações auxiliam no diagnóstico, ajuste de parâmetros e acompanhamento do comportamento do mecanismo.

---

### 🦾 Gripper — Garra Articulada

A garra foi projetada para possuir diferentes posições angulares, permitindo adaptar sua orientação de acordo com a posição de coleta e o nível de operação do elevador.

O mecanismo possui três posições de referência:

- ⬇️ **Posição Inferior:** orientação voltada para coleta de game pieces em posições mais baixas.
- ➖ **Posição Intermediária:** orientação mais horizontal, utilizada para posicionamento e movimentação do mecanismo.
- ⬆️ **Posição Superior:** orientação inclinada para cima, permitindo adaptar o posicionamento da garra.

A integração entre o elevador e a garra permite que os mecanismos sejam utilizados em diferentes configurações de operação do robô.

### 🎯 SEQUENCIAIS AUTOMÁTICOS

O robô possui 3 sequenciais (SequentialL1, SequentialL2 e SequentialL3), um para cada nível do elevador. Cada sequencial é um grupo de comandos que coordena a garra e o elevador em ordem, a partir de um único botão.

Etapas de cada sequencial:

A garra vai para o ângulo de referência.
A garra executa a coleta da game piece.
O elevador se movimenta até o setpoint do nível.
O estado do elevador é atualizado (Alvo_L1, Alvo_L2 ou Alvo_L3).
A garra vai para o ângulo final do nível.

---

## 🧰 Tecnologias utilizadas

- **Java** — Linguagem de programação utilizada no desenvolvimento do software.
- **WPILib** — Framework utilizado no desenvolvimento de robôs FRC.
- **REV Robotics** — Biblioteca e controladores utilizados no sistema do elevador.
- **Swerve Drive** — Sistema de movimentação omnidirecional.
- **SmartDashboard** — Monitoramento e análise de informações durante os testes.

---

## 👨‍💻 Desenvolvimento

Projeto desenvolvido pela equipe MTECH #9603 no contexto da FIRST Robotics Competition.

O software reúne implementação de subsistemas, controle de mecanismos, gerenciamento de estados e integração dos sistemas do robô.
