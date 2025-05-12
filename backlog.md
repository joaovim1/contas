# 📘 Backlog – Sistema de Controle de Despesas (Front-end + API)


---

## ✅ Funcionalidades Concluídas

### 📝 Cadastro de nova despesa
- [x] Formulário com `descrição`, `valor` e `data`
- [x] Envio via `POST /despesas`

### 🛠 Edição de despesa
- [x] Preenche formulário com dados existentes
- [x] Atualiza via `PUT /despesas/{id}`

### ❌ Exclusão de despesa
- [x] Botão para apagar despesa individual
- [x] Requisição `DELETE /despesas/{id}`

### 📄 Listagem de despesas
- [x] Carrega despesas automaticamente
- [x] Renderiza com `li`, botão de pagar e editar

### 🔍 Filtro por mês e ano
- [x] Selects `#mesFiltro` e `#anoFiltro`
- [x] Filtragem por `GET /despesas?mes=MM&ano=YYYY`

### 💰 Controle de pagamento
- [x] Botão alterna entre “Pago” e “Pagar”
- [x] Usa `PATCH /despesas/{id}` com `{ pago: true/false }`
- [x] Ícone muda no hover

### 📊 Totais exibidos
- [x] 💵 Total do mês (`totalGasto`)
- [x] 🟥 Total a pagar (`total`)
- [x] 🟩 Total pago (`totalPago`)
- [x] 🚬 Total gasto com "Cigarro" (`totalCigarro`)

### 💵 Formatação de valor
- [x] Input com máscara: `000.000.000.000.000,00`
- [x] Exibição em `BRL` com `toLocaleString`

### 📅 Formatação de data
- [x] Exibição de data como `dd/mm/yyyy`
- [x] Conversão com função `formatDate(despesa.data)`

---

## 🔧 Em Desenvolvimento

### 🗓 Correção de fuso horário na data
- [ ] Corrigir data sendo salva com -1 dia por causa do UTC no `input[type=date]`

---

## 🧩 Funcionalidades a Fazer

### 🏷 Categorias (Tipo de despesa)
- [ ] Adicionar campo `tipo` ao formulário
- [ ] Mostrar tipo na lista de despesas
- [ ] Opção de editar tipo existente
- [ ] Backend: incluir campo `tipo` no model

### 🟢 Total por tipo (ex: VR)
- [ ] Calcular total apenas para `tipo === 'VR'`
- [ ] Mostrar abaixo dos totais gerais:
  - `🟢 Total VR: R$ XXX,XX`

### 🔐 Sistema de Login
- [ ] Criar tela de login com email e senha
- [ ] Enviar credenciais para `POST /auth/login`
- [ ] Salvar `token` no localStorage
- [ ] Adicionar `Authorization` nos headers das requisições
- [ ] Bloquear acesso ao sistema se não estiver autenticado
- [ ] Exibir nome do usuário no cabeçalho

---

## 📥 Funcionalidades Futuras

### 🧾 Upload de comprovantes
- [ ] Upload de imagem/documento de uma despesa
- [ ] Visualização do comprovante
- [ ] Backend: endpoint de upload + campo na despesa

### 📤 Exportar dados
- [ ] Exportar despesas como `.csv` ou `.xlsx`
- [ ] Filtros aplicados devem refletir no arquivo

---

## 🧠 Notas Técnicas

- Função `loadDespesas()` atualiza a interface após qualquer operação.
- Os botões de “pagar” e “cancelar” usam eventos `mouseover` e `mouseout` para feedback visual.
- Despesas com `descricao.includes("CIGARRO")` são computadas em `totalCigarro`.
- Exemplo da estrutura HTML usada na listagem:

```html
<li style="border-left: 5px solid red;">
  <span>Mercado - R$ 250,00 - 11/05/2025</span>
  <button>Editar</button>
  <button>Excluir</button>
  <button class="toggle-pago-btn">Pagar</button>
</li>
