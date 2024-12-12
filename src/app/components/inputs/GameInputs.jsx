import { Input, Switch } from '@nextui-org/react';
import { IoMdHeart, IoMdHeartEmpty } from 'react-icons/io';

import AchivementsInput from '@/app/components/inputs/AchivementsInput';
import BacklogInput from '@/app/components/inputs/BacklogInput';
import TagInput from '@/app/components/inputs/TagInput';

export default function GameInputs({
  isFavorite,
  setIsFavorite,
  isFinished,
  setIsFinished,
  backlog,
  setBacklog,
  achievements,
  setAchievements,
  totalAchievements,
  setTotalAchievements,
  timePlayed,
  setTimePlayed,
  tags,
  setTags,
}) {
  return (
    <>
      <div className="flex md:flex-row flex-col gap-4 justify-between">
        {/* FAVORITE */}
        <Switch size="lg" color="success" startContent={<IoMdHeart />} endContent={<IoMdHeartEmpty />} isSelected={isFavorite} onValueChange={setIsFavorite}>
          Favorito
        </Switch>

        {/* FINISHED */}
        <Switch size="lg" color="success" isSelected={isFinished} onValueChange={setIsFinished}>
          Terminado
        </Switch>
      </div>

      {/* BACKLOG */}
      <BacklogInput backlog={backlog} setBacklog={setBacklog} achievements={achievements} totalAchievements={totalAchievements} />

      {/* ACHIEVEMENTS */}
      <AchivementsInput achievements={achievements} setAchievements={setAchievements} totalAchievements={totalAchievements} setTotalAchievements={setTotalAchievements} />

      {/* TIME PLAYED */}
      <Input
        label="Tiempo jugado"
        placeholder="Introduce el tiempo jugado en minutos"
        type="number"
        variant="bordered"
        onChange={(e) => setTimePlayed(e.target.value)}
        value={timePlayed ? timePlayed : 0}
        endContent={'min'}
        description={`Tiempo total: ${Math.floor(timePlayed / 60)}h ${timePlayed % 60}min ${timePlayed / 60 / 24 > 1 ? ` (${Math.floor(timePlayed / 60 / 24)} días)` : ''}`}
      />

      {/* TAGS */}
      <TagInput tags={tags} setTags={setTags} />
    </>
  );
}
