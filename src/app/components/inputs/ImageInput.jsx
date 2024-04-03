import { Button, Image, Input } from '@nextui-org/react';
import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from 'react-icons/md';

export default function ImageInput({ customImage, setCustomImage, imageKey, setImageKey, grids, viewOnly }) {
  const passImage = (increment) => {
    setImageKey((prevKey) => (prevKey + increment + grids.length) % grids.length);
  };

  return (
    <>
      <Image width={600} height={900} alt="Hero Image" src={customImage || grids[imageKey]} />
      <Input label="Imagen personalizada" placeholder="Introduzca una URL de sgdb.com" type="text" variant="bordered" onChange={(e) => setCustomImage(e.target.value)} value={customImage} required />
      {customImage ? (
        <Button onClick={() => setCustomImage('')} className="h-10 w-full text-xl block z-40">
          Borrar imagen personalizada
        </Button>
      ) : (
        <div className="flex justify-between items-center">
          <Button onClick={() => passImage(-1)} className="flex items-center justify-center text-4xl z-40" isIconOnly>
            <MdKeyboardArrowLeft />
          </Button>
          <div className={'w-1/4'}>
            {!viewOnly && (
              <Input
                value={imageKey + 1}
                onChange={(e) => {
                  setImageKey(e.target.value - 1);
                }}
                endContent={
                  <div className={'flex-row flex gap-2'}>
                    <p>{' / '}</p>
                    <p>{grids.length}</p>
                  </div>
                }
                classNames={{
                  inputWrapper: 'h-unit-10',
                }}
              />
            )}
          </div>
          <Button onClick={() => passImage(+1)} className="flex items-center justify-center text-4xl z-40" isIconOnly>
            <MdKeyboardArrowRight />
          </Button>
        </div>
      )}
    </>
  );
}
