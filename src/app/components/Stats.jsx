'use client';

import { Card, CardBody, CardHeader, Divider, Switch } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';

import Chart from '@/app/components/Chart';
import GroupListSection from '@/app/components/sections/GroupListSection';

export default function Stats({ data, gData }) {
  const [bl1, setBl1] = useState(true);
  const [bl2, setBl2] = useState(true);
  const [bl3, setBl3] = useState(true);
  const [open, setOpen] = useState(false);

  const filterData = () => {
    return {
      numOfGames: (data?.numOfGames.backlogNA || 0) + (bl1 ? data?.numOfGames.backlog1 || 0 : 0) + (bl2 ? data?.numOfGames.backlog2 || 0 : 0) + (bl3 ? data?.numOfGames.backlog3 || 0 : 0),

      numOfPlayedGames:
        (data?.numOfPlayedGames.backlogNA || 0) +
        (bl1 ? data?.numOfPlayedGames.backlog1 || 0 : 0) +
        (bl2 ? data?.numOfPlayedGames.backlog2 || 0 : 0) +
        (bl3 ? data?.numOfPlayedGames.backlog3 || 0 : 0),

      numOfTotalAchivements:
        (data?.numOfTotalAchivements.backlogNA || 0) +
        (bl1 ? data?.numOfTotalAchivements.backlog1 || 0 : 0) +
        (bl2 ? data?.numOfTotalAchivements.backlog2 || 0 : 0) +
        (bl3 ? data?.numOfTotalAchivements.backlog3 || 0 : 0),

      numOfTotalTime:
        (data?.numOfTotalTime.backlogNA || 0) + (bl1 ? data?.numOfTotalTime.backlog1 || 0 : 0) + (bl2 ? data?.numOfTotalTime.backlog2 || 0 : 0) + (bl3 ? data?.numOfTotalTime.backlog3 || 0 : 0),

      numOfCompletedAchivements:
        (data?.numOfCompletedAchivements.backlogNA || 0) +
        (bl1 ? data?.numOfCompletedAchivements.backlog1 || 0 : 0) +
        (bl2 ? data?.numOfCompletedAchivements.backlog2 || 0 : 0) +
        (bl3 ? data?.numOfCompletedAchivements.backlog3 || 0 : 0),

      numOfCompletedGames: data?.numOfCompletedGames || 0,
      numOfFinishedGames: data?.numOfFinishedGames || 0,
    };
  };

  const dataFiltered = filterData();

  const calculateTime = (totalMinutes) => {
    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;
    return { hours, minutes };
  };

  const calculateAverageTime = (totalMinutes, numOfPlayedGames) => {
    const hours = numOfPlayedGames > 0 ? Math.floor(totalMinutes / 60 / numOfPlayedGames) : 0;
    const minutes = numOfPlayedGames > 0 ? Math.floor(totalMinutes / numOfPlayedGames) % 60 : 0;
    return { hours, minutes };
  };

  useEffect(() => {
    filterData();
  }, [bl1, bl2, bl3]);

  return (
    <section className="h-full w-full items-center justify-center relative p-5">
      <h1 className="text-5xl w-full text-center mb-5 uppercase">Estadisticas</h1>
      <div className="w-full flex items-center justify-center">
        <Card className="h-fit w-[50%] bg-gray-600/20 p-2">
          <CardHeader className="text-2xl">Selecciona los backlogs que deseas incluir en las estadisticas</CardHeader>
          <Divider />
          <CardBody className="flex flex-row justify-evenly">
            <Switch isSelected={bl1} onValueChange={setBl1}>
              Backlog 1
            </Switch>
            <Switch isSelected={bl2} onValueChange={setBl2}>
              Backlog 2
            </Switch>
            <Switch isSelected={bl3} onValueChange={setBl3}>
              Backlog 3
            </Switch>
          </CardBody>
        </Card>
      </div>
      <div className="h-full w-full flex flex-row flex-wrap p-5 gap-5 justify-center">
        <div className="flex flex-col gap-4 p-2 h-fit w-[25%]">
          <Card className="flex flex-col gap-4 p-2 w-full h-fit bg-gray-600/20">
            <CardHeader className="w-full text-center text-2xl uppercase">Tiempo</CardHeader>
            <Divider />
            <CardBody>
              <p className="text-2xl">
                Total: {calculateTime(dataFiltered.numOfTotalTime).hours} h {calculateTime(dataFiltered.numOfTotalTime).minutes} min
              </p>
              <p className="text-2xl">
                Media por juego: {calculateAverageTime(dataFiltered.numOfTotalTime, dataFiltered.numOfPlayedGames).hours} h{' '}
                {calculateAverageTime(dataFiltered.numOfTotalTime, dataFiltered.numOfPlayedGames).minutes} min
              </p>
            </CardBody>
          </Card>
          <Card className="flex flex-col gap-4 p-2 w-full bg-gray-600/20 ">
            <CardHeader className="w-full text-center text-2xl uppercase">Logros</CardHeader>
            <Divider />
            <CardBody>
              <Chart fullData={dataFiltered.numOfTotalAchivements} obteinedData={dataFiltered.numOfCompletedAchivements} labelObtenied="Desbloqueados" labelFull="Bloqueados" />
            </CardBody>
          </Card>
        </div>

        <div className="flex flex-col gap-4 p-2 h-fit w-[25%]">
          <Card className="flex flex-col gap-4 p-2 h-fit bg-gray-600/20">
            <CardHeader className="w-full text-center text-2xl uppercase">Completacionismo</CardHeader>
            <Divider />
            <CardBody>
              <p className="text-2xl">Juegos Completados: {dataFiltered.numOfCompletedGames}</p>
              <p className="text-2xl">Juegos Terminados: {dataFiltered.numOfFinishedGames}</p>
            </CardBody>
          </Card>

          <Card className="flex flex-col gap-4 p-2 bg-gray-600/20 ">
            <CardHeader className="w-full text-center text-2xl uppercase">Juegos</CardHeader>
            <Divider />
            <CardBody>
              <Chart fullData={dataFiltered.numOfGames} obteinedData={dataFiltered.numOfPlayedGames} labelObtenied="Jugados" labelFull="Sin jugar aun" />
            </CardBody>
          </Card>
        </div>
      </div>
      {gData && <GroupListSection gData={gData} open={open} setOpen={setOpen} />}
    </section>
  );
}
