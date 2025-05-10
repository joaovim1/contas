<template>
  <div>
    <h2>Controle de Despesas</h2>
    <button @click="showForm = !showForm">{{ showForm ? 'Cancelar' : 'Adicionar Despesa' }}</button>
    <div v-if="showForm">
      <form @submit.prevent="saveDespesa">
        <input v-model="descricao" placeholder="Descrição" required />
        <input v-model="valor" placeholder="Valor" required />
        <input v-model="data" type="date" required />
        <button type="submit">Salvar</button>
      </form>
    </div>
    <ul>
      <li v-for="despesa in despesas" :key="despesa.id">
        {{ despesa.descricao }} - {{ despesa.valor }} - {{ despesa.data }}
        <button @click="editDespesa(despesa)">Editar</button>
        <button @click="deleteDespesa(despesa.id)">Excluir</button>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      despesas: [],
      showForm: false,
      descricao: '',
      valor: '',
      data: '',
      despesaId: null,
    };
  },
  methods: {
    fetchDespesas() {
      axios.get('/despesas')
        .then(response => {
          this.despesas = response.data;
        })
        .catch(error => {
          console.error('Erro ao carregar despesas:', error);
        });
    },
    saveDespesa() {
      const despesa = { descricao: this.descricao, valor: this.valor, data: this.data };
      if (this.despesaId) {
        axios.put(`/despesas/${this.despesaId}`, despesa)
          .then(() => {
            this.fetchDespesas();
            this.resetForm();
          });
      } else {
        axios.post('/despesas', despesa)
          .then(() => {
            this.fetchDespesas();
            this.resetForm();
          });
      }
    },
    editDespesa(despesa) {
      this.descricao = despesa.descricao;
      this.valor = despesa.valor;
      this.data = despesa.data;
      this.despesaId = despesa.id;
      this.showForm = true;
    },
    deleteDespesa(id) {
      axios.delete(`/despesas/${id}`)
        .then(() => {
          this.fetchDespesas();
        });
    },
    resetForm() {
      this.descricao = '';
      this.valor = '';
      this.data = '';
      this.despesaId = null;
      this.showForm = false;
    },
  },
  mounted() {
    this.fetchDespesas();
  },
};
</script>
