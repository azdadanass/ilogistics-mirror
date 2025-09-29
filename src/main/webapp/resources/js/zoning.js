//---------------------------
//1. SETUP PIXI APP
//---------------------------
const app = new PIXI.Application({
	background: '#ffffff',
	resizeTo: window,
	antialias: true,
});
//document.body.appendChild(app.view);

document.getElementById('zoningGird').appendChild(app.view);

//---------------------------
//2. INPUT DATA
//---------------------------
const warehouseData = [{ "id": 401, "reference": "L1C1H1", "fillPercentage": 0.5 }, { "id": 402, "reference": "L1C1H2", "fillPercentage": 0.3 }, { "id": 403, "reference": "L1C1H3", "fillPercentage": 0.8 }, { "id": 404, "reference": "L1C1H4", "fillPercentage": 1.0 }, { "id": 405, "reference": "L1C2H1", "fillPercentage": 0.0 }, { "id": 406, "reference": "L1C2H2", "fillPercentage": 0.0 }, { "id": 407, "reference": "L1C3H1", "fillPercentage": 0.0 }, { "id": 408, "reference": "L1C3H2", "fillPercentage": 0.0 }, { "id": 409, "reference": "L1C3H3", "fillPercentage": 0.0 }, { "id": 410, "reference": "L1C3H4", "fillPercentage": 0.0 }, { "id": 411, "reference": "L1C4H1", "fillPercentage": 0.0 }, { "id": 412, "reference": "L1C4H2", "fillPercentage": 0.0 }, { "id": 413, "reference": "L1C4H3", "fillPercentage": 0.0 }, { "id": 414, "reference": "L1C4H4", "fillPercentage": 0.0 }, { "id": 415, "reference": "L1C5H1", "fillPercentage": 0.0 }, { "id": 416, "reference": "L1C5H2", "fillPercentage": 0.0 }, { "id": 417, "reference": "L1C5H3", "fillPercentage": 0.0 }, { "id": 418, "reference": "L2C1H1", "fillPercentage": 0.0 }, { "id": 419, "reference": "L2C1H2", "fillPercentage": 0.0 }, { "id": 420, "reference": "L2C1H3", "fillPercentage": 0.0 }, { "id": 421, "reference": "L2C2H1", "fillPercentage": 0.0 }, { "id": 422, "reference": "L2C2H2", "fillPercentage": 0.0 }, { "id": 423, "reference": "L2C2H3", "fillPercentage": 0.0 }, { "id": 424, "reference": "L2C2H4", "fillPercentage": 0.0 }, { "id": 425, "reference": "L2C3H1", "fillPercentage": 0.0 }, { "id": 426, "reference": "L2C3H2", "fillPercentage": 0.0 }, { "id": 427, "reference": "L2C3H3", "fillPercentage": 0.0 }, { "id": 428, "reference": "L2C3H4", "fillPercentage": 0.0 }, { "id": 429, "reference": "L2C4H1", "fillPercentage": 0.0 }, { "id": 430, "reference": "L2C4H2", "fillPercentage": 0.0 }, { "id": 431, "reference": "L2C4H3", "fillPercentage": 0.0 }, { "id": 432, "reference": "L2C4H4", "fillPercentage": 0.0 }, { "id": 433, "reference": "L2C5H1", "fillPercentage": 0.0 }, { "id": 434, "reference": "L2C5H2", "fillPercentage": 0.0 }, { "id": 435, "reference": "L2C5H3", "fillPercentage": 0.0 }, { "id": 436, "reference": "L2C5H4", "fillPercentage": 0.0 }, { "id": 437, "reference": "L2C6H1", "fillPercentage": 0.0 }, { "id": 438, "reference": "L2C6H2", "fillPercentage": 0.0 }, { "id": 439, "reference": "L2C6H3", "fillPercentage": 0.0 }, { "id": 440, "reference": "L2C6H4", "fillPercentage": 0.0 }, { "id": 441, "reference": "L3C1H1", "fillPercentage": 0.0 }, { "id": 442, "reference": "L3C1H2", "fillPercentage": 0.0 }, { "id": 443, "reference": "L3C1H3", "fillPercentage": 0.0 }, { "id": 444, "reference": "L3C1H4", "fillPercentage": 0.0 }, { "id": 445, "reference": "L3C2H1", "fillPercentage": 0.0 }, { "id": 446, "reference": "L3C2H2", "fillPercentage": 0.0 }, { "id": 447, "reference": "L3C2H3", "fillPercentage": 0.0 }, { "id": 448, "reference": "L3C2H4", "fillPercentage": 0.0 }, { "id": 449, "reference": "L3C3H1", "fillPercentage": 0.0 }, { "id": 450, "reference": "L3C3H2", "fillPercentage": 0.0 }, { "id": 451, "reference": "L3C3H3", "fillPercentage": 0.0 }, { "id": 452, "reference": "L3C3H4", "fillPercentage": 0.0 }, { "id": 453, "reference": "L3C4H1", "fillPercentage": 0.0 }, { "id": 454, "reference": "L3C4H2", "fillPercentage": 0.0 }, { "id": 455, "reference": "L3C4H3", "fillPercentage": 0.0 }, { "id": 456, "reference": "L3C4H4", "fillPercentage": 0.0 }, { "id": 457, "reference": "L3C5H1", "fillPercentage": 0.0 }, { "id": 458, "reference": "L3C5H2", "fillPercentage": 0.0 }, { "id": 459, "reference": "L3C5H3", "fillPercentage": 0.0 }, { "id": 460, "reference": "L3C5H4", "fillPercentage": 0.0 }, { "id": 461, "reference": "L4C1H1", "fillPercentage": 0.0 }, { "id": 462, "reference": "L4C1H2", "fillPercentage": 0.0 }, { "id": 463, "reference": "L4C1H3", "fillPercentage": 0.0 }, { "id": 464, "reference": "L4C1H4", "fillPercentage": 0.0 }];

//---------------------------
//3. PARSE REFERENCE STRINGS
//---------------------------
function parseRef(ref) {
	const match = ref.match(/L(\d+)C(\d+)H(\d+)/);
	if (!match) return null;
	return {
		line: parseInt(match[1]),
		column: parseInt(match[2]),
		height: parseInt(match[3]),
	};
}

const parsed = warehouseData.map(d => ({
	...d,
	...parseRef(d.reference)
}));

//Get max values for layout
const maxLine = Math.max(...parsed.map(p => p.line));
const maxColumn = Math.max(...parsed.map(p => p.column));
const maxHeight = Math.max(...parsed.map(p => p.height));

//---------------------------
//4. GRID CONFIG
//---------------------------
const CELL_WIDTH = 50;
const CELL_HEIGHT = 25;
const GAP_X = 10;
const GAP_Y = 10;
const OFFSET_X = 100;
const OFFSET_Y = 80;

//Fill colors by percentage
function getColor(fill) {
	if (fill < 0.2) return 0x2ecc71; // green
	if (fill < 0.8) return 0x3498db; // blue
	if (fill < 1.0) return 0xe67e22; // orange
	return 0xe74c3c; // red
}

const cells = [];

//---------------------------
//5. DRAW CELLS
//---------------------------
parsed.forEach(item => {
	const rect = new PIXI.Graphics();
	rect.beginFill(getColor(item.fillPercentage));
	rect.drawRect(0, 0, CELL_WIDTH, CELL_HEIGHT);
	rect.endFill();

	// Compute position
	const x = OFFSET_X + (item.column - 1) * (CELL_WIDTH + GAP_X);
	const y =
		OFFSET_Y +
		(item.line - 1) * ((CELL_HEIGHT + GAP_Y) * maxHeight) +
		(item.height - 1) * (CELL_HEIGHT + 3);

	rect.x = x;
	rect.y = y;
	rect.cursor = 'pointer';
	rect.eventMode = 'static';
	rect.cellId = item.id;
	rect.refLabel = item.reference;
	rect.fillPercentage = item.fillPercentage;

	// Click → log ID
	rect.on('pointerdown', () => {
		console.log(`Clicked cell ID: ${rect.cellId}, Fill: ${rect.fillPercentage * 100}%`);
		PF('deleteDlg').show();
	});

	// Hover → show tooltip
	rect.on('pointerover', () => showTooltip(`${rect.refLabel} — ${Math.round(rect.fillPercentage * 100)}%`));
	rect.on('pointermove', (e) => moveTooltip(e.global));
	rect.on('pointerout', hideTooltip);

	app.stage.addChild(rect);
	cells.push(rect);
});

//---------------------------
//6. LINE & COLUMN LABELS
//---------------------------
const textStyle = new PIXI.TextStyle({
	fill: '#000000',
	fontSize: 14,
	fontWeight: 'bold'
});

//Column labels (top)
for (let c = 1; c <= maxColumn; c++) {
	const label = new PIXI.Text(`Col ${c}`, textStyle);
	label.x = OFFSET_X + (c - 1) * (CELL_WIDTH + GAP_X) + 5;
	label.y = OFFSET_Y - 30;
	app.stage.addChild(label);
}

//Line labels (left)
for (let l = 1; l <= maxLine; l++) {
	const label = new PIXI.Text(`Line ${l}`, textStyle);
	label.x = 20;
	label.y = OFFSET_Y + (l - 1) * ((CELL_HEIGHT + GAP_Y) * maxHeight) + 20;
	app.stage.addChild(label);
}

//---------------------------
//7. TOOLTIP SYSTEM
//---------------------------
const tooltip = document.createElement('div');
tooltip.style.position = 'fixed';
tooltip.style.padding = '6px 10px';
tooltip.style.background = 'rgba(0,0,0,0.8)';
tooltip.style.color = '#fff';
tooltip.style.borderRadius = '6px';
tooltip.style.fontFamily = 'sans-serif';
tooltip.style.fontSize = '13px';
tooltip.style.pointerEvents = 'none';
tooltip.style.transition = 'opacity 0.1s ease';
tooltip.style.opacity = '0';
tooltip.style.zIndex = '1000';
document.body.appendChild(tooltip);

function showTooltip(text) {
	tooltip.textContent = text;
	tooltip.style.opacity = '1';
}

/*function moveTooltip(pos) {
tooltip.style.left = pos.x + 12 + 'px';
tooltip.style.top = pos.y + 12 + 'px';
}*/

function moveTooltip(pos) {
	// Get canvas position relative to the page
	const rect = app.view.getBoundingClientRect();

	// Adjust tooltip coordinates based on canvas position
	tooltip.style.left = rect.left + pos.x + 12 + 'px';
	tooltip.style.top = rect.top + pos.y + 12 + 'px';
}

function hideTooltip() {
	tooltip.style.opacity = '0';
}