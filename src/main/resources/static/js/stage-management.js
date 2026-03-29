document.addEventListener('DOMContentLoaded', () => {
    fetchStages();

    const stageForm = document.getElementById('stageForm');
    if (stageForm) {
        stageForm.addEventListener('submit', handleStageSubmit);
    }
});

async function fetchStages() {
    try {
        const response = await fetch('/rest/api/stages');
        if (!response.ok) throw new Error('Failed to fetch stages');

        const stages = await response.json();
        renderStageTable(stages);
    } catch (error) {
        console.error('Error:', error);

    }
}

function renderStageTable(stages) {
    const tableBody = document.getElementById('stageListTable');
    if (!tableBody) return;

    tableBody.innerHTML = '';

    stages.forEach(stage => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${stage.id}</td>
            <td>${stage.name}</td>
            <td>${stage.ordering}</td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-warning me-2"
                        onclick="editStage('${stage.id}', '${stage.name}', ${stage.ordering})">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger"
                        onclick="deleteStage('${stage.id}')">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

async function handleStageSubmit(e) {
    e.preventDefault();

    const form = e.target;

    const stageData = {
        id: document.getElementById('stageId').value,
        name: document.getElementById('stageName').value,
        ordering: parseInt(document.getElementById('stageOrdering').value) || 1
    };

    const isEdit = !!form.dataset.editId;

    const url = isEdit ? `/rest/api/stages/${form.dataset.editId}` : '/rest/api/stages';
    const method = isEdit ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(stageData)
        });

        if (response.ok) {
            alert(isEdit ? 'Stage updated successfully!' : 'Stage created successfully!');

            resetForm('stageForm');

            fetchStages();
        } else {
            const errorData = await response.json();
            alert('Error: ' + (errorData.message || 'Operation failed. Check if ID already exists.'));
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Server error: Could not connect to the database.');
    }
}

function editStage(id, name, ordering) {
    const form = document.getElementById('stageForm');
    const idInput = document.getElementById('stageId');
    const nameInput = document.getElementById('stageName');
    const orderInput = document.getElementById('stageOrdering');
    const submitBtn = form.querySelector('button[type="submit"]');

    form.dataset.editId = id;

    idInput.value = id;
    idInput.disabled = true;
    nameInput.value = name;
    orderInput.value = ordering;

    submitBtn.textContent = "Update Stage";
    submitBtn.classList.replace('btn-primary', 'btn-warning');

    form.scrollIntoView({ behavior: 'smooth' });
}

async function deleteStage(id) {
    if (!confirm(`Are you sure you want to delete stage: ${id}?`)) return;

    try {
        const response = await fetch(`/rest/api/stages/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            fetchStages();
        } else {
            alert('Failed to delete stage. It might be linked to existing events.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Server error occurred during deletion.');
    }
}