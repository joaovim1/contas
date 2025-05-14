$(document).ready(function () {
//favor salvar
const apiUrl = 'https://painelcontas.onrender.com/despesas';

$('#valor').mask('000.000.000.000.000,00', { reverse: true });
$('#btn-incluir').hide();
const senhaHtml = `
    <div id="senha-container" style="margin-bottom: 15px;">
        <input type="password" id="senha-acesso" placeholder="Digite a senha para incluir" style="padding: 5px;">
        <button id="verificar-senha" style="padding: 5px 10px; margin-left: 5px;">OK</button>
    </div>
`;

$('#btn-incluir').before(senhaHtml);

$('#verificar-senha').on('click', function () {
    const senhaCorreta = 'admin';
    const senhaDigitada = $('#senha-acesso').val();

    if (senhaDigitada === senhaCorreta) {
        $('#btn-incluir').show();
        $('#senha-container').remove();
    } else {
        alert('Senha incorreta!');
    }
});
loadDespesas();

$('#filtro-categoria').on('change', function () {
    loadDespesas();
    changeDivFilter();
});

$('#mesFiltro').on('change', function () {
    loadDespesas();
});

$('#anoFiltro').on('change', function () {
    loadDespesas();
});

$('#despesa-form').submit(function (e) {
    e.preventDefault();

    const rawValor = $('#valor').val().replace(/\./g, '').replace(',', '.');
    const tipo = $('#tipo').is(':checked') ? 'VR' : 'NORMAL';
    const despesaData = {
        descricao: $('#descricao').val(),
        valor: rawValor,
        data: $('#data').val(),
        tipo: tipo
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
} else if (ano) {
    url += `?ano=${ano}`;
}

fetch(url)
    .then(response => response.json())
    .then(despesas => {
        const filtroSelecionado = $('#filtro-categoria').val();

        despesas = despesas.filter(d => {
            switch (filtroSelecionado) {
                case 'vr':
                    return d.tipo === 'VR';
                case 'pagas':
                    return d.pago === true && d.tipo !== 'VR';
                case 'naoPagas':
                    return d.pago === false && d.tipo !== 'VR';
                case 'cigarro':
                    return d.descricao?.toUpperCase().includes('CIGARRO');
                case 'todas':
                    default:
                        return true;
            }
    });


        const lista = $('#despesas-lista');
        lista.empty();

        let total = 0;
        let totalCigarro = 0;
        let totalPago = 0;
        let totalGasto = 0;
        let totalVR = 0;

        despesas.forEach(despesa => {
            const valorNumerico = parseFloat(despesa.valor);
            const pago = despesa.pago === true;
            const tipo = despesa.tipo;
            if(tipo !== 'VR') totalGasto += valorNumerico;
            
            if (!pago && tipo !== 'VR') total += valorNumerico;
            if (pago && tipo === 'VR') totalVR += valorNumerico;
            if (despesa.descricao?.toUpperCase().includes('CIGARRO') && pago) {
                totalCigarro += valorNumerico;
            }
            if (pago && tipo !== 'VR') totalPago += valorNumerico;

            const tipoVR = despesa.tipo === 'VR' ? ' - VR' : '';
            const item = $(`
                <li style="border-left: ${pago ? '5px solid green' : '5px solid red'};">
                    <span>${despesa.descricao}${tipoVR} - ${formatCurrency(valorNumerico)} - ${formatDate(despesa.data)}</span>
                </li>
            `);

            const botao = item.find('.toggle-pago-btn');
            botao.data({ id: despesa.id, pago });

            botao.on('click', function () {
                const id = $(this).data('id');
                const pagoAtual = $(this).data('pago');
                const novoPago = !pagoAtual;

                fetch(`${apiUrl}/${id}`, {
                    method: 'PATCH',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ pago: novoPago })
                })
                .then(() => {
                    loadDespesas();
                })  
                .catch(() => alert('Erro ao atualizar o pagamento.'));
            });

            botao.on('mouseover', function () {
                if ($(this).data('pago') === true) {
                    $(this).html('<i class="fas fa-times-circle"></i> Cancelar Pagamento');
                }
            });

            botao.on('mouseout', function () {
                const pagoAtual = $(this).data('pago');
                $(this).html(pagoAtual
                    ? '<i class="fas fa-check-circle"></i> Pago'
                    : '<i class="fas fa-money-bill-wave"></i> Pagar');
            });

            lista.append(item);
        });

        $('#valor-total-mes').text(`${formatCurrency(totalGasto)}`);
        $('#valor-total').text(`${formatCurrency(total)}`);
        $('#valor-cigarro').text(`${formatCurrency(totalCigarro)}`);
        $('#valor-pago').text(`${formatCurrency(totalPago)}`);
        $('#valor-vr').text(`${formatCurrency(totalVR)}`)
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
    if (!date) return '';
    return date.split('T')[0].split('-').reverse().join('/');
}

function formatDateDois(date) {
    if (!date) return '';
    return date.split('T')[0].split('-').reverse().join('/');
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
            $('#tipo').prop('checked', despesa.tipo === 'VR');

            $('#pago').val(despesa.pago);
            $('#form-title').text('Editar Despesa');
            showForm();
            loadDespesas();
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
    $('#tipo').prop('checked', false);
    $('#form-title').text('Adicionar Despesa');
}

function changeDivFilter() {
    const filtroSelecionado = $('#filtro-categoria').val();

    
    $("#card-total-mes").hide();
    $("#card-total").hide();
    $("#card-pago").hide();
    $("#card-cigarro").hide();
    $("#card-vr").hide();

    
    if (filtroSelecionado === "vr") {
        $("#card-total-mes").show();
        $("#card-pago").show();
        $("#card-vr").show();
    } else if (filtroSelecionado === "todas") {
        $("#card-total-mes").show();
        $("#card-total").show();
        $("#card-pago").show();
        $("#card-cigarro").show();
        $("#card-vr").show();
    } else if (filtroSelecionado === "pagas") {
        $("#card-total-mes").show();
        $("#card-pago").show();
    } else if (filtroSelecionado === "naoPagas") {
        $("#card-total").show();
    } else if (filtroSelecionado === "cigarro") {
        $("#card-total-mes").show();
        $("#card-pago").show();
        $("#card-cigarro").show();
    }
}

window.showForm = showForm;
window.showList = showList;
window.editDespesa = editDespesa;
window.deleteDespesa = deleteDespesa;
window.resetForm = resetForm;
window.changeDivFilter = changeDivFilter;

});