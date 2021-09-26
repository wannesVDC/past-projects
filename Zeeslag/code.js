// 0: water, 1: miss, 2: hit, 3: sunken, 4: boot
var world = {
    A : leegVeld(),
    B : leegVeld(),
    gameState : -1, //0: game in progress, 1: game won, 2: game lost
    boatsA : [],
    boatsB : []
    
}

function init(){
    world.A = leegVeld();
    world.B = leegVeld();
    world.gameState = 0;
    world.boatsA = randomBotenLijst();
    world.boatsB = randomBotenLijst();

    var taken = takenCoord(world.boatsA);
    for (var i=0 ; i<taken.length ; i++){
        if(0 <= taken[i][0] && taken[i][0] < 10 && 0 <= taken[i][1] && taken[i][1] < 10){
            world.A[taken[i][0]][taken[i][1]] = 4;
        }
    }
    var taken = takenCoord(world.boatsB);
    for (var i=0 ; i<taken.length ; i++){
        if(0 <= taken[i][0] && taken[i][0] < 10 && 0 <= taken[i][1] && taken[i][1] < 10){
            world.B[taken[i][0]][taken[i][1]] = 4;
        }
    }

    machineGun.stateA = 0
    machineGun.turnsA = 3
    machineGun.turnsB = 0
    machineGun.countA = 0
    machineGun.countB = 0

    startTime();
    draw();
}

function startGame(){
    init();
}

function drawClock(){
    var timeString = useTimer();
    document.getElementById("timer").innerHTML = timeString;
    setTimeout(drawClock,10);
}

function draw(){
    
    drawPlayingField(0);
    drawPlayingField(1);

    if(world.gameState == 1){
        setTimeout(function win(){alert("Congratulations!\n Press start new game to play again.")}, 500);
        endGame();
    }
    else if (world.gameState == 2){
        setTimeout(function loss(){alert("Too bad!\n Press start new game to play again.")}, 500);
        endGame();
    }

    if (machineGun.turnsA >= 3){
        document.getElementById("machineGun").disabled = false;
    }
    else{
        document.getElementById("machineGun").disabled = true;
    }
    
}

function endGame(){
    stopTime();
}

function leegVeld(){// return an empty playing field
    var b = [];
    for (var i=0 ; i<10 ; i++){
        b.push([0,0,0,0,0 ,0,0,0,0,0]);
    }
    return b;
}

function randomBotenLijst(){// return a randomized list of all the boats (with checking for overlap etc)
    var list = [[]];

    list.push(randomBootCoord(6,list));

    list.push(randomBootCoord(4,list));
    list.push(randomBootCoord(4,list));

    list.push(randomBootCoord(3,list));
    list.push(randomBootCoord(3,list));
    list.push(randomBootCoord(3,list));

    list.push(randomBootCoord(2,list));
    list.push(randomBootCoord(2,list));
    list.push(randomBootCoord(2,list));
    list.push(randomBootCoord(2,list));
    
    var possible = true;
    for (var i=0 ; i<list.length ; i++){
        if (list[i] == [-1,-1,-1,-1]){
            possible == false;
        }
    }
    if (!possible){
        list = randomBotenLijst();
    }

    return list;
}

function randomBootCoord(size,boats){// return the fist coord + direction + size of a boat with given size
    i = Math.floor(10*Math.random()); //create random number between 0 and 9
    j = Math.floor(10*Math.random());

    r = Math.floor(4*Math.random()); // 0: links, 1:boven, 2:rechts, 3:onder
    
    var count = 0;
    while(!isValidBoat(i,j,r,size,boats)){
        i = Math.floor(10*Math.random());
        j = Math.floor(10*Math.random());

        r = Math.floor(4*Math.random());

        count += 1;
        if (count >= 1000){
            return [-1,-1,-1,-1];
        }
    }

    return [i,j,r,size];

}

function isValidBoat(i,j,r,size,boats){
    var valid = true;
    for (var c=0 ; c<size ; c++){
        if(r == 0){
            valid = (valid && freeBox(i,j-c,r,boats,c));
        }
        else if(r == 1){
            valid = (valid && freeBox(i-c,j,r,boats,c));
        }
        else if(r == 2){
            valid = (valid && freeBox(i,j+c,r,boats,c));
        }
        else if(r == 3){
            valid = (valid && freeBox(i+c,j,r,boats,c));
        }
    }

    return valid;
}

function freeBox(i,j,r,boats,c){// returns wether a certain box is good to place a boat on 
    var valid = true;
    var lastBox = [];
    if (i < 0 || i >= 10 || j < 0 || j >= 10){
        valid = false;
    }
    
    if (c > 0){
        if(r == 0){
            lastBox = [i,j+1];
        }
        if(r == 1){
            lastBox = [i+1,j];
        }
        if(r == 2){
            lastBox = [i,j-1];
        }
        if(r == 3){
            lastBox = [i-1,j];
        }
    }
    for (var a=-1 ; a<2 ; a++){
        for (var b=-1 ; b<2 ; b++){
            if(c == 0 || ([i+a,j+b] != lastBox) ){
                if (!(i+a < 0 || i+a >= 10 || j+b < 0 || j+b >= 10)){
                    if (valid){
                        valid = freeCoord(i+a,j+b,boats);
                    }   
                }
            }
        }
    }

    return valid;
}

function freeCoord(i,j, boats){// returns true if the coord doesn't include a boat
    if (!(i < 0 || i >= 10 || j < 0 || j >= 10)){
        taken = takenCoord(boats);
        for (var a=0 ; a<taken.length ; a++){
            if (taken[a][0] == i && taken[a][1] == j){
                return false;
            }
        }
    }
    return true;
}

function takenCoord(boats){// returns a list of coords that contain boats
    var list = [];
        for (var i=0 ; i<boats.length ; i++){
            for (var j=0 ; j<boats[i][3] ; j++){
                if (boats[i][2] == 0){
                    list.push([boats[i][0] , boats[i][1]-j]);
                }
                if (boats[i][2] == 1) {
                    list.push([boats[i][0] - j , boats[i][1]]);
                }
                if (boats[i][2] == 2) {
                    list.push([boats[i][0] , boats[i][1]+j]);
                }
                if (boats[i][2] == 3) {
                    list.push([boats[i][0] + j , boats[i][1]]);
                }
            }
        }
    return list;
}

function boatContains(i,j,player){// returns the coords of the boat which coord was entered \\ invalid output betekend dat het geen boatCoord was
    if (player == 0){
            for (var a=0 ; a<world.boatsA.length ; a++){
                var list = getBoatCoords(a,player);
                for (var b=0 ; b<list.length ; b++){
                    if (list[b][0] == i && list[b][1] == j){
                        return list;
                    }
                }
            }
        }
    if (player ==1){
            for (var a=0 ; a<world.boatsB.length ; a++){
                var list = getBoatCoords(a,player);
                for (var b=0 ; b<list.length ; b++){
                    if (list[b][0] == i && list[b][1] == j){
                        return list;
                    }
                }
            }
        }
    return [-1,-1];
}

function getBoatCoords(boatNumber,player){
    var list = [];
        if (player == 0){
            var i = world.boatsA[boatNumber][0];
            var j = world.boatsA[boatNumber][1];
            var r = world.boatsA[boatNumber][2];
            var size = world.boatsA[boatNumber][3];

            for (var a=0 ; a<size ; a++){
                if (r == 0){
                    list.push([i , j-a]);
                }
                else if (r == 1){
                    list.push([i-a , j]);
                }
                else if (r == 2){
                    list.push([i , j+a]);
                }
                else if (r == 3){
                    list.push([i+a , j]);
                }
            }
        }
        else if (player == 1){
            i = world.boatsB[boatNumber][0];
            j = world.boatsB[boatNumber][1];
            r = world.boatsB[boatNumber][2];
            size = world.boatsB[boatNumber][3];

            for (var a=0 ; a<size ; a++){
                if (r == 0){
                    list.push([i , j-a]);
                }
                else if (r == 1){
                    list.push([i-a , j]);
                }
                else if (r == 2){
                    list.push([i , j+a]);
                }
                else if (r == 3){
                    list.push([i+a , j]);
                }
            }
        }
    return list;
}

function drawPlayingField(player){
    if (player == 0){
            stringTabel = "";
            stringTabel += "<thead class=\"outOfFrame\"><tr><th>Speler A</th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th><th>F</th><th>G</th><th>H</th><th>I</th><th>J</th></th></thead>";

            for (var j=0 ; j<10 ; j++){
                stringTabel += "<tr><th class=\"outOfFrame\">"+(j+1)+"</th>";

                for (var i=0 ; i<10 ; i++){
                    if (world.A[j][i] == 0){//water
                        stringTabel += "<td> </td>";
                    }
                    else if (world.A[j][i] == 1){//miss
                        stringTabel += "<td class=\"miss\">  </td>";
                    }
                    else if (world.A[j][i] == 2){//hit
                        stringTabel += "<td class=\"hit\">  </td>";
                    }
                    else if (world.A[j][i] == 3){//sunken
                        stringTabel += "<td class=\"sunken\">  </td>";
                    }
                    else if (world.A[j][i] == 4){//boat
                        stringTabel += "<td class=\"boat\">  </td>";
                    }
                }

                stringTabel += "</tr>";
            }            
            document.getElementById("A").innerHTML = stringTabel;
        }
        else{
            stringTabel = "";
            stringTabel += "<thead class=\"outOfFrame\"><tr><th>Speler B</th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th><th>F</th><th>G</th><th>H</th><th>I</th><th>J</th></th></thead>";

            for (var j=0 ; j<10 ; j++){
                stringTabel += "<tr><th class=\"outOfFrame\">"+(j+1)+"</th>";

                for (var i=0 ; i<10 ; i++){
                    if (world.B[j][i] == 0 || world.B[j][i] == 4){//water of boten dat niet getekend worden
                        stringTabel += "<td onclick=\"beurtA("+ j + "," + i +")\"> </td>";
                    }
                    else if (world.B[j][i] == 1){//miss
                        stringTabel += "<td class=\"miss\">  </td>";
                    }
                    else if (world.B[j][i] == 2){//hit
                        stringTabel += "<td class=\"hit\">  </td>";
                    }
                    else if (world.B[j][i] == 3){//sunken
                        stringTabel += "<td class=\"sunken\">  </td>";
                    }
                }
                stringTabel += "</tr>";
            }
            document.getElementById("B").innerHTML = stringTabel;
        }
}

function beurtA(i,j){
    if(world.B[i][j] == 4){
        world.B[i][j] = 2;
        boatSunk(i,j,1);
    }
    else if (world.B[i][j] == 0){
        world.B[i][j] = 1;
    }
    if (machineGun.stateA == 1){
        machineGun.turnsA = 0
        machineGun.countA += 1;
        if (machineGun.countA >= 5){
            machineGun.stateA = 0;
            machineGun.countA = 0;
        }
    }
    else{
        machineGun.turnsA += 1
    }
    
    setGameState();
    draw();

    if (world.gameState == 0 && machineGun.stateA == 0){
        machineGun.turnsB += 1;
        beurtB();
        setGameState();
        draw();

        if(machineGun.turnsB == 3* Math.floor(machineGun.turnsB/3)){//turns divisible by 3
            machineGun.countB += 1;
            while(machineGun.countB < 5){
                beurtB();
                setGameState();
                draw();
                machineGun.countB += 1;
            }
            machineGun.countB = 0;
        }
    }
}

function setGameState(){
    var aBoats = false;
    var bBoats = false;
    for (var i =0 ; i <10 ; i++){
        for (var j =0 ; j <10 ; j++){
            if (world.A[i][j] == 4){
                aBoats = true;
            }
            if (world.B[i][j] == 4){
                bBoats = true;
            }
        }
    }
    if (!aBoats){
        world.gameState = 2;
    }
    else if (!bBoats){
        world.gameState = 1;
    }
}

function beurtB(){
    var i = Math.floor(10*Math.random());
    var j = Math.floor(10*Math.random());

    while (world.A[i][j] == 1 || world.A[i][j] == 2 || world.A[i][j] == 3){
        var i = Math.floor(10*Math.random());
        var j = Math.floor(10*Math.random());
    }

    if(world.A[i][j] == 4){
        world.A[i][j] = 2;
        boatSunk(i,j,0);
    }
    else if (world.A[i][j] == 0){
        world.A[i][j] =1;
    }
}

function boatSunk(i,j,playField){
    var sunk = true;
    var checkList = boatContains(i,j,playField);

    for (var counter=0 ; counter<checkList.length ; counter++){
        iCheck = checkList[counter][0];
        jCheck = checkList[counter][1];
        if(0 <= iCheck && iCheck < 10 && 0 <= jCheck && jCheck < 10){
            if (playField == 0){
                sunk = (sunk && world.A[checkList[counter][0]][checkList[counter][1]] == 2);
            }
            if (playField == 1){
                sunk = (sunk && world.B[checkList[counter][0]][checkList[counter][1]] == 2);
            }
        }
        else{
            sunk = false;
        }
    }
    
    if (sunk){
        for (var counter=0 ; counter<checkList.length ; counter++){
            if (playField == 0){
                world.A[checkList[counter][0]][checkList[counter][1]] = 3;
            }
            else if (playField == 1){
                world.B[checkList[counter][0]][checkList[counter][1]] = 3;
            }
        }
    }
}


var stopwatch = {
    start : new Date().getTime(),
    time : 0,
    state : 0, //0 is inactive. 
    refresh : 0
}

function startTime(){
    stopwatch.start = new Date().getTime();
    stopwatch.time = 0;
    stopwatch.state = 1;
}

function stopTime(){
    stopwatch.state = 0;
}

function increaseTime(){
    stopwatch.time = (new Date().getTime()) - stopwatch.start;
}

function getTimeString(){
    var time = stopwatch.time;
    var hours = Math.floor(time / (3600000));
    time -= (3600000*hours);
    var minutes = Math.floor(time / 60000);
    time -= (60000*minutes);
    var seconds = Math.floor(time / 1000);
    time -= (1000*seconds);
    return hours+" hours "+minutes+" minutes "+seconds+" seconds "+time+" miliseconds";
}

function useTimer(){
    if (stopwatch.state == 1){
        increaseTime();
    }
    return getTimeString();
}

var machineGun = {
    stateA : 0, //0: inactive, 1: active
    turnsA : 3, // turns since last use, to make button active of inactive
    turnsB : 0, // active in turn 3,6,9,...
    countA : 0,
    countB : 0
}

function activateMachineGun(){
    machineGun.stateA = 1
    draw()
}