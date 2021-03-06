/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.statemachine.transition;

import java.util.Collection;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.trigger.EventTrigger;
import org.springframework.statemachine.trigger.Trigger;
import org.springframework.util.Assert;

/**
 * Base implementation of a {@link Transition}.
 * 
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public abstract class AbstractTransition<S, E> implements Transition<S, E> {

	private final State<S,E> source;

	private final State<S,E> target;

	private final Collection<Action<S, E>> actions;

	private final TransitionKind kind;
	
	private final Guard<S, E> guard;

	private Trigger<S, E> trigger;

	public AbstractTransition(State<S, E> source, State<S, E> target, Collection<Action<S, E>> actions, E event,
			TransitionKind kind, Guard<S, E> guard) {
		Assert.notNull(source, "Source must be set");
//		Assert.notNull(target, "Target must be set");
		Assert.notNull(kind, "Transition type must be set");
		this.source = source;
		this.target = target;
		this.actions = actions;
		if (event != null) {
			this.trigger = new EventTrigger<S, E>(event);
		}
		this.kind = kind;
		this.guard = guard;
	}

	@Override
	public State<S,E> getSource() {
		return source;
	}

	@Override
	public State<S,E> getTarget() {
		return target;
	}

	@Override
	public Collection<Action<S, E>> getActions() {
		return actions;
	}

	@Override
	public Trigger<S, E> getTrigger() {
		return trigger;
	}

	@Override
	public boolean transit(StateContext<S, E> context) {
		if (guard != null) {
			if (!guard.evaluate(context)) {
				return false;
			}
		}
		if (actions != null) {
			for (Action<S, E> action : actions) {
				action.execute(context);
			}
		}
		return true;
	}

	@Override
	public TransitionKind getKind() {
		return kind;
	}

}
