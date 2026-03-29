document.addEventListener('DOMContentLoaded', () => {
    fetchResults();
    loadResultEvents();

    const resultForm = document.getElementById('resultForm');
    if (resultForm) {
        resultForm.addEventListener('submit', handleResultSubmit);
    }

    const eventSelect = document.getElementById('resultEventId');
    if (eventSelect) {
        eventSelect.onchange = (e) => {
            const selectedOption = e.target.options[e.target.selectedIndex];
            const sport = selectedOption ? selectedOption.getAttribute('data-sport') : null;
            renderDynamicFields(sport);
        };
    }
});

async function loadResultEvents() {
    try {
        const response = await fetch('/rest/api/events?size=100');
        const result = await response.json();
        const events = result.data || [];

        const select = document.getElementById('resultEventId');
        if (!select) return;

        select.innerHTML = '<option value="" selected disabled>Select Event</option>';

        events.forEach(event => {
            const opt = document.createElement('option');
            opt.value = event.id;
            opt.setAttribute('data-sport', event.sportName || event.sportType);
            opt.textContent = `${event.homeTeam.name} vs ${event.awayTeam.name} (${event.dateVenue})`;
            select.appendChild(opt);
        });
    } catch (error) {
        console.error("Error loading events:", error);
    }
}

function parseList(elementId) {
    const element = document.getElementById(elementId);
    if (!element || !element.value.trim()) return [];
    return element.value.split(',').map(s => s.trim()).filter(Boolean);
}

function toSlug(text) {
    if (!text) return "tbd";
    return text
        .toLowerCase()
        .trim()
        .replace(/\s+/g, '-')
        .replace(/[^a-z0-9-]/g, '');
}


function renderDynamicFields(sport, data = null) {
    const container = document.getElementById('dynamicResultFields');
    if (!container) return;

    const sportKey = sport ? sport.toUpperCase() : '';
    let html = '';

    switch (sportKey) {
        case 'FOOTBALL':
        case 'SOCCER':
            html = `
                <div class="row">
                    <div class="col-6 mb-3"><label class="form-label">Home Goals</label><input type="number" class="form-control" id="homeGoals" value="${data?.homeGoals ?? 0}" required></div>
                    <div class="col-6 mb-3"><label class="form-label">Away Goals</label><input type="number" class="form-control" id="awayGoals" value="${data?.awayGoals ?? 0}" required></div>
                </div>
                <div class="mb-3"><label class="form-label">Goals (Comma separated)</label><input type="text" class="form-control" id="goalsList" value="${(data?.goals || []).join(', ')}" placeholder="e.g. Messi 10', Pelé 20'"></div>
                <div class="mb-3"><label class="form-label">Yellow Cards</label><input type="text" class="form-control" id="yellowCards" value="${(data?.yellowCards || []).join(', ')}"></div>
            `;
            break;

        case 'BASKETBALL':
            html = `
                <div class="row">
                    <div class="col-6 mb-3"><label class="form-label">Home Points</label><input type="number" class="form-control" id="homeTotalPoints" value="${data?.homeTotalPoints ?? 0}" required></div>
                    <div class="col-6 mb-3"><label class="form-label">Away Points</label><input type="number" class="form-control" id="awayTotalPoints" value="${data?.awayTotalPoints ?? 0}" required></div>
                </div>
                <div class="mb-3"><label class="form-label">Quarter Points</label><input type="text" class="form-control" id="quarterPoints" value="${(data?.quarterPoints || []).join(', ')}" placeholder="20, 25, 15, 30"></div>
            `;
            break;

        case 'TENNIS':
            html = `
                <div class="row">
                    <div class="col-6 mb-3"><label class="form-label">Home Sets</label><input type="number" class="form-control" id="homeSets" value="${data?.homeSets ?? 0}" required></div>
                    <div class="col-6 mb-3"><label class="form-label">Away Sets</label><input type="number" class="form-control" id="awaySets" value="${data?.awaySets ?? 0}" required></div>
                </div>
                <div class="mb-3"><label class="form-label">Set Scores</label><input type="text" class="form-control" id="setScores" value="${(data?.setScores || []).join(', ')}" placeholder="6-4, 2-6, 7-5"></div>
            `;
            break;

        default:
            html = '<p class="text-muted text-center border p-3">Select an event above to enter specific scores</p>';
    }

    container.innerHTML = html;
}

async function handleResultSubmit(e) {
    e.preventDefault();

    const form = e.target;
    const eventSelect = document.getElementById('resultEventId');
    const selectedOption = eventSelect.options[eventSelect.selectedIndex];

    if (!selectedOption || selectedOption.disabled) {
        alert("Please select a valid event.");
        return;
    }

    const sport = selectedOption.getAttribute('data-sport').toUpperCase();
    const eventId = parseInt(eventSelect.value);

    let resultData = {
        eventId: eventId,
        sportType: sport,
        winner: toSlug(document.getElementById('resultWinner').value),
        message: document.getElementById('resultMessage').value || ""
    };

    if (sport === 'FOOTBALL' || sport === 'SOCCER') {
        resultData.homeGoals = parseInt(document.getElementById('homeGoals').value) || 0;
        resultData.awayGoals = parseInt(document.getElementById('awayGoals').value) || 0;
        resultData.goals = parseList('goalsList');
        resultData.yellowCards = parseList('yellowCards');
    } else if (sport === 'BASKETBALL') {
        resultData.homeTotalPoints = parseInt(document.getElementById('homeTotalPoints').value) || 0;
        resultData.awayTotalPoints = parseInt(document.getElementById('awayTotalPoints').value) || 0;
        const qp = document.getElementById('quarterPoints').value;
        resultData.quarterPoints = qp ? qp.split(',').map(s => parseInt(s.trim())).filter(n => !isNaN(n)) : [];
    } else if (sport === 'TENNIS') {
        resultData.homeSets = parseInt(document.getElementById('homeSets').value) || 0;
        resultData.awaySets = parseInt(document.getElementById('awaySets').value) || 0;
        resultData.setScores = parseList('setScores');
    }

    const editId = form.dataset.editId;
    const url = editId ? `/rest/api/results/${editId}` : '/rest/api/results';
    const method = editId ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(resultData)
        });

        if (response.ok) {
            alert(editId ? "✅ Result updated!" : "✅ Result saved!");
            resetResultForm();
            fetchResults();
        } else {
            const err = await response.json();
            alert("Error: " + (err.message || "Could not save result"));
        }
    } catch (error) {
        console.error("Submit error:", error);
        alert("Server communication error.");
    }
}


async function fetchResults() {
    try {
        const response = await fetch('/rest/api/results');
        const results = await response.json();
        renderResultTable(results);
    } catch (error) {
        console.error("Error fetching results:", error);
    }
}

function renderResultTable(results) {
    const tableBody = document.getElementById('resultListTable');
    if (!tableBody) return;
    tableBody.innerHTML = '';

    results.forEach(res => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${res.id}</td>
            <td><span class="badge bg-info text-dark">${res.sportType || 'N/A'}</span></td>
            <td><strong>${res.winner}</strong></td>
            <td><small>${res.message || '-'}</small></td>
            <td class="text-end">
                <button class="btn btn-sm btn-warning me-1" onclick="prepareEditResult(${res.id})">Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deleteResult(${res.id})">Del</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}
