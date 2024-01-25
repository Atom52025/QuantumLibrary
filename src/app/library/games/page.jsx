import { Checkbox, CheckboxGroup } from '@nextui-org/react';

import Image from 'next/image';

import { Button } from '@nextui-org/button';

async function getData(category) {
  const url = 'http://localhost:8080/api/games' + (category != null ? '?category=' + category : '');
  const res = await fetch(url, { cache: 'no-store' });
  if (!res.ok) {
    throw new Error('Failed to fetch data');
  }
  return res.json();
}

export default async function Page({ searchParams }) {
  const data = await getData(searchParams?.category);
  const gridClass = 'aspect-[6/9] bg-gray-600 rounded-md relative overflow-hidden group';
  return (
    <>
      <div className="h-full min-w-[200px] bg-gray-800/30 shadow-inner flex flex-col p-1">
        <CheckboxGroup label="Tags Filters" defaultValue={['buenos-aires', 'london']}>
          <Checkbox value="buenos-aires">Buenos Aires</Checkbox>
          <Checkbox value="london">London</Checkbox>
          <Checkbox value="paris">Paris</Checkbox>
        </CheckboxGroup>
      </div>
      <main className="min-h-full w-full ">
        <div className="p-10 grid grid-cols-6 gap-3">
          {data.games.map((game) => (
            <div key={game.id} className={gridClass}>
              <div className="z-10 w-full h-full relative">
                <Image src={game.image} alt={game.image} fill priority={true}></Image>
              </div>
              <div className="z-20 absolute bottom-0 w-full text-center font-bold uppercase bg-gray-900/40 p-1 opacity-0 group-hover:opacity-100">{game.name}</div>
            </div>
          ))}
          <div className={gridClass + ' border border-dashed border-gray-400 flex justify-center items-center'}>
            <Button>
              <div className="text-4xl font-bold">+</div>
              <div className="text-xl">Add Game</div>
            </Button>
          </div>
        </div>
      </main>
    </>
  );
}
