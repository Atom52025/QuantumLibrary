import Image from 'next/image';



export default async function Page() {
  const data = await getData();
  const gridClass = 'aspect-[6/9] bg-gray-600 rounded-md relative overflow-hidden group';
  return <main className="bg-gray-900 min-h-full"></main>;
}
