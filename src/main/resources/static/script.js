$(document).ready(function () {
    const apiUrl = 'http://localhost:8080/despesas';

    $('#valor').mask('000.000.000.000.000,00', { reverse: true });

    loadDespesas();

    $('#mesFiltro').on('change', function () {
        loadDespesas();
    });

    $('#anoFiltro').on('change', function () {
        loadDespesas();
    });

    $('#despesa-form').submit(function (e) {
        e.preventDefault();

        const rawValor = $('#valor').val().replace(/\./g, '').replace(',', '.');

        const despesaData = {
            descricao: $('#descricao').val(),
            valor: rawValor,
            data: $('#data').val()
        };

        const method = $('#form-title').text() === 'Adicionar Despesa' ? 'POST' : 'PUT';
        const url = method === 'POST' ? apiUrl : `${apiUrl}/${$('#descricao').data('id')}`;

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(despesaData)
        })
        .then(response => response.json())
        .then(() => {
            alert('Despesa salva!');
            loadDespesas();
            resetForm();
            showList();
        })
        .catch(() => {
            alert('Erro ao salvar a despesa!');
        });
    });

    function loadDespesas() {
        let url = apiUrl;
        const mes = $('#mesFiltro').val();
        const ano = $('#anoFiltro').val();

        if (mes && ano) {
            url += `?mes=${parseInt(mes)}&ano=${ano}`;
        }
        else if (ano) {
            url += `?ano=${ano}`;
        }
        else if (mes === "") {
            url += `?ano=${ano}`;
        }

        fetch(url)
            .then(response => response.json())
            .then(despesas => {
                const lista = $('#despesas-lista');
                lista.empty();
                let total = 0;
                let totalCigarro = 0; 

                despesas.forEach(despesa => {
                    const valorNumerico = parseFloat(despesa.valor);
                    total += valorNumerico;

                   
                    if (despesa.descricao && despesa.descricao.toUpperCase().includes('CIGARRO')) {
                        totalCigarro += valorNumerico; 
                    }

                    const item = $(`
                        <li>
                            <span>${despesa.descricao} - ${formatCurrency(valorNumerico)} - ${formatDate(despesa.data)}</span>
                            <button onclick="editDespesa(${despesa.id})">Editar</button>
                            <button onclick="deleteDespesa(${despesa.id})">Excluir</button>
                            
                        </li>
                    `);
                    lista.append(item);
                });

                
                $('#valor-total').text(`Total Despesa: ${formatCurrency(total)}`);

                
                $('#valor-cigarro').text(`Total gasto com Cigarro: ${formatCurrency(totalCigarro)}`);
            })
            .catch(() => {
                alert('Erro ao carregar despesas.');
            });
    }

    function formatCurrency(value) {
        return value.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        });
    }

    function formatDate(date) {
        const d = new Date(date);
        const day = ("0" + d.getDate()).slice(-2);
        const month = ("0" + (d.getMonth() + 1)).slice(-2);
        const year = d.getFullYear();
        return `${day}/${month}/${year}`;
    }

    function deleteDespesa(id) {
        fetch(`${apiUrl}/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            alert('Despesa excluída!');
            loadDespesas();
        })
        .catch(() => {
            alert('Erro ao excluir a despesa!');
        });
    }

    function editDespesa(id) {
        fetch(`${apiUrl}/${id}`)
            .then(response => response.json())
            .then(despesa => {
                $('#descricao').val(despesa.descricao).data('id', despesa.id);
                $('#valor').val(formatCurrencyRaw(despesa.valor));
                $('#data').val(despesa.data);
                $('#form-title').text('Editar Despesa');
                showForm();
            })
            .catch(() => {
                alert('Erro ao carregar os dados da despesa!');
            });
    }

    function formatCurrencyRaw(value) {
        return parseFloat(value).toFixed(2).replace('.', ',');
    }

    function showForm() {
        $('#formulario').show();
        $('#listar').hide();
    }

    function showList() {
        $('#formulario').hide();
        $('#listar').show();
    }

    function resetForm() {
        $('#descricao').val('').removeData('id');
        $('#valor').val('');
        $('#data').val('');
        $('#form-title').text('Adicionar Despesa');
    }

    window.showForm = showForm;
    window.showList = showList;
    window.editDespesa = editDespesa;
    window.deleteDespesa = deleteDespesa;
});
